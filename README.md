# sb-microservices-jwt-example

Example project to demonstrate how JWT auth can be used in microservices with
SpringBoot.

It's a made-up video streaming platform with no any functionality implemented.
The goal is to simply demonstrate how JWT auth can be used in a microservices
project with scaling perspective. Design is multi-tenant for a more realistic
example to see how auth tokens of different tenants can be isolated from each
other.

Additionally, we'll set up api-key auth mechanism to allow external
systems to integrate with us. This is just to help to demonstrate how multiple
auth mechanisms can coexist. JWTs are for end users, while api-keys are
for external systems. I did not pay much attention to the api-keys, so they
might not work end-to-end, but all the key pieces in the auth design for them
are there, so you can have a good grasp of how multiple auth mechanisms
can coexist along with JWTs.

Note that realistically, I'd recommended translating
api-keys to short-lived JWTs on the api-gateway level or/and services filters
level to avoid excessive calls to `auth-service` for requests that span multiple
services. But it's not the purpose of this example.

# Disclaimer

This is a simplified example project. I've cut a lot of corners that are not
relevant to the authentication/authorization topic. You won't see typical
microservices approaches for resiliency, data consistency, etc. like
circuit breakers, retries, idempotency (well, idempotency may be used
somewhere subconsciously), saga, transactional outbox, etc. This is intentional,
and for a real system you absolutely should take care of these things if you
want it to be production ready.

Also, I did not use any generated code for rest clients so it's easier to
examine what is travelling over the wire. So you can easily put a breakpoint
and see what data is being sent and received, and if necessary, modify the code
to play with it.

Some services are not implemented at all. The Initial picture I had in mind was
larger than what I ended up with. And I realized that full auth implementation
is already there and nicely demonstratable with the current set of services, no
need to implement more services. So I stopped at that point.

# Intended usage

The intended way to examine how JWTs work here is to start the services in
debug mode, put breakpoints in the key places, and step through the code
execution to see what is happening at each step.

# Design

## Roles and permissions design

Several roles are defined that are placed in JWT claims:

- sa - super-admin - admin with access to all operations across all tenants
- adm - admin - admin with access to all operations across its tenant only
- cmg - content-manager - role for managing content
- umg - user-manager - role for managing users
- usr - user - regular user (watch movies) role
- srv - service - special role for microservices, used for calls initiated by
  services

Note that the roles are hardcoded (`Role` enum) and are not
hierarchical to keep things simple since this is not the subject of this example
project.

Each microservice will interpret these roles as a set of more fine-grained
permissions specialized for this particular service. Such fine-grained
permissions specific to the service in spring-security are called
`GrantedAuthority`.

In a more complex scenario where customers might need to define their own roles
with custom hierarchy, it would require developing a more complex solution with
"roles" and "permissions" and corresponding endpoints to manipulate them.
But in the end, `auth-service` will still be putting the list of resolved
"permissions" for the user in the JWT, and the auth mechanism will be
exactly the same. The difference would be in complexity of `auth-service`,
how it calculates the final list of what to put in the JWT claims.
But auth mechanics would stay the same.

Current simplified roles design (`GA` stands for `GrantedAuthority`):

```
User => [role1, role2] => JWT(role1, role2) => Service => 
    AuthMngr maps [role1, role2] to [GA1, GA2, GA3...]  =>
    SecurityCfg defines security rules in terms of GA1, GA2, GA3...
```

Design with hierarchical custom roles and permissions:

```
User => role1,role2 => 
  auth-service: role1=[p1, p2], role2=[role3, role4], role3=[p7], role4=[p2] =>
  JWT(p1, p2, p7) => Service =>
  AuthMngr maps [p1, p2, p7] to [GA1, GA2, GA3...]  =>
  SecurityCfg defines security rules in terms of GA1, GA2, GA3...
```

Only the step of calculating JWT contents is different here.

## service tokens

Service token is a JWT token with exactly the same structure as the regular user
JWT token. The only difference is that it has a special role `srv` in the roles
claim, and uses special pre-defined values for `subject`, `tid` (tenant uuid) and
`did` (device id). Service tokens always belong to the `service` tenant,
have `service` device id, and `subject` is one of constants defined in
`ReservedIdentifiers`.

Service tokens are used in two cases:

- when a call is initiated by a service without any user calls involved.
  Example: some periodic cleanup, data expiration, data archiving, etc.
- when a user initiates a call, and as a part of the request processing service
  needs to make a call to a protected endpoint of another service that requires
  more privileges, and such a call is expected. Example: unauthenticated user
  requests registration, and `user-service` needs to call `auth-service` to
  register the user. In this case, `user-service` will use a service token to
  call `auth-service`. CreateUser endpoint in `auth-service` can't be
  unprotected because of security reasons, but the caller user can't be
  authenticated yet because he doesn't exist in `auth-service`
  (chicken-egg problem).

## key pieces to examine

We basically have three key pieces of spring-security to make all this work:

- filters - extract auth information from request. The most important lesson
  for JWTs here is that there are NO any DB/external calls are made. All the
  necessary auth information is contained in the JWT itself.
    - `TenantUuidExtractionFilter` - extracts tenant UUIDs of the caller and
      target resource from request header/params. Caller's tenant UUID in
      `x-tenant-uuid` header takes effect only for unprotected endpoints that
      are accessible without a token. Protected endpoints extract tenant UUID
      from JWT/api-key. And `tenantUuid` query param is used to specify target
      resource's tenant UUID in case of cross-tenant calls in some scenarios
      (for example, service initiated calls, super-admin creates new
      tenant, etc).
    - `JwtFilter` - extracts JWT token and parses+validates it. Extracts
      all the relevant information from the JWT and populates it. Builds
      `Authentication` object (`JwtAuthentication` impl) out of the data
      contained in JWT, and sets it in the security context so that
      `AuthenticationManager` can process it later when deciding whether to
      allow access to the endpoint or not. Delegates some work to
      `JwtAuthExtractor`, it's a reusable piece that we use for handling
      messaging metadata as well.
    - `ApiKeyFilter` - extracts api-key from request header. Then makes a call
      to `auth-service` to fetch all the roles of the api-key. Note that a
      service JWT token is used in this call, and this endpoint is not public.
      So only services (and api-gateway) can request api-key validation.
- auth providers (`JwtAuthProvider`) - maps `roles` from our definition to
  `GrantedAuthority` that spring-security understands and operates with.
- security filter chain configuration (`SecurityCfg` in each microservice) -
  defines which endpoints are protected, and which `GrantedAuthorities` are
  required to access them. Note that access rules are defined in terms of
  `GrantedAuthorities`. Pay attention to the custom helpers `AuthZFactory`
  and `AuthZAnyOf` usage - they add some global special handling of `SA` and
  `ADMIN` roles, and tenant isolation so that users of one tenant
  can't access data of other tenants.
- how auth is integrated into messaging - see below.

## messaging

At first, I wanted to avoid using messaging for simplicity's sake. But at some
point having multiple rest calls to hardcoded endpoints to notify of
tenant creation/deletion/etc. became too ugly. Additionally, showcasing how
nicely JWT auth fits into messaging is a good idea.

I decided to use redis pub/sub for messaging here ONLY for setup simplicity, as
it doesn't require any additional configuration, it works out of the box.
In a real-world scenario, redis doesn't fit for `PointToPointMsgQueue` use case
since it's not durable/persistent. Moreover, it works as a simple "fan out"
messaging which fits better for `PubSubMsgQueue` use case.

Ideally, `PointToPointMsgQueue` should be implemented with something like
Kafka, RabbitMQ, GCP PubSub, AWS SQS, etc. Because in practically all cases
where it needs to be applied, it has to be durable with guaranteed delivery.
Whereas `PubSubMsgQueue` implementation depends on the use-case, and redis
implementation may be fine for it.

In regard to authentication, see `PointToPointMsgQueueRedisImpl` and
`MsgRedisImpl`, examine what happens with the message and its metadata
before it is actually processed by the consumer. And how the metadata is
populated when the message is sent.

# Running

## Pre-requisites

I tried to make it as easy to run as possible.

- install:
    - java-21 (I highly recommend https://sdkman.io for unix systems)
    - mongodb (you can either install it directly or use docker, doesn't matter,
      no any setup is required, just make sure that you run it on default
      port 27017)
    - redis (same as mongo - install or run in docker, no setup, run on default
      port 6379)
    - latest python 3 (old 3+ versions might work, I didn't test) for running
      password reset script
- open your IDE and register installed java-21
- clone the repository
- import the project into your IDE as gradle project, and configure it to use
  java-21 as both the project SDK and the JVM for gradle
- put breakpoints in the key places to examine the code execution (see below)
- start all the implemented services in debug mode (`auth-service`, `user-service`,
  `tenant-service`, `otp-service`, `email-service`)
- perform initial setup (see below)
- run any of the functional tests you're interested in from `test` directory and
  step through the code execution to see how the auth works
- additionally, you can examine redis and mongo databases to watch how the data
  is being created and updated. But it's not really necessary for the auth flow.
  For mongo, you can use `mongodb compass` GUI tool, or `mongosh` for CLI.
  For redis, you can use `redis-cli` for terminal or `redisinsight` for GUI.

## Initial setup

Tests always create all the data from scratch - tenants, users, etc. In order to
do so, tests use SA user (that's the only standard built-in user that can
create new tenants). Hence, to run tests, you need to reset password for SA
and export it as an environment variable `JWT_EXAMPLE_SA_PASSWORD` for
the test process.

All the initial data is set up either by the functional tests or by data
migration code **automatically**. Tests always create all the data from 
scratch - tenants, users, etc. In order to do so, tests use SA user
(that's the only standard built-in user that can create new tenants). Hence, 
to run tests, you need to reset password for SA and export it as an 
environment variable `JWT_EXAMPLE_SA_PASSWORD` for the test process.
This can be done by running the `scripts/sa-reset-password.py` script, which 
makes API calls to reset the password by the way. It's a legit scenario, not 
a backdoor, so feel free to examine it too.

Remember the SA password you set. Functional tests require
`JWT_EXAMPLE_SA_PASSWORD` environment variable to be set with the SA password.
The easier option is to configure it in your IDE rather than messing up with
the OS. To do that in Intellij IDEA, run one of the tests and wait until it
fails with "`JWT_EXAMPLE_SA_PASSWORD` is not set" error. Now IDE has
automatically created a run configuration for the test. To edit it, go to
`Run -> Edit Configurations...`, select the test run configuration, choose
the one for running test, and in the `Environment variables` field add

```
JWT_EXAMPLE_SA_PASSWORD=<YOUR_SA_PASSWORD>
```

For example:

```
JWT_EXAMPLE_SA_PASSWORD=QX1p!@Tri01w@ABxMRw^&*2f%
```

Now run this same configuration again, and now everything should work fine.

I could of course hardcoded some initial SA password, but I couldn't stand such
a bad security practice even in a sample project. I wanted to
demonstrate how the password reset works, and how the initial data is set up
in a real-world scenario.

# tests

Functional tests are located in `test` folder. They are supposed to be executed
against a fully running system.
