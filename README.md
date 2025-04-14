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
for external systems.

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

- when a call is initiated by a service without any user calls. Example: some
  periodic cleanup, data expiration, data archiving, etc.
- when a user initiates a call, and as a part of the request processing service
  needs to make a call to a protected endpoint of another service that requires
  more privileges, and such a call is expected. Example: unauthenticated user
  requests registration, and `user-service` needs to call `auth-service` to
  register the user. In this case, `user-service` will use a service token to
  call `auth-service`. CreateUser endpoint in `auth-service` can't be
  unprotected because of security reasons, but the caller user can't be
  authenticated yet because he doesn't exist in `auth-service`
  (chicken-egg problem).

## spring-security implementation

We basically have three key pieces of spring-security to make all this work:

- filters - extract auth information from request
    - `TenantUuidHeaderExtractionFilter` - extracts tenant UUID from request
      header. Takes effect only for unprotected endpoints that are accessible
      without a token. Protected endpoints extract tenant UUID from JWT/api-key.
    - `JwtFilter` - extracts JWT token and parses+validates it. Extracts
      all the relevant information from the JWT and populates it. Builds
      `Authentication` object (`JwtAuthentication` impl) out of the data
      contained in JWT, and sets it in the security context so that
      `AuthenticationManager` can process it later when deciding whether to
      allow access to the endpoint or not.
    - `ApiKeyFilter` - extracts api-key from request header. Then makes a call
      to `auth-service` to fetch all the roles of the api-key. Note that a
      service JWT token is used in this call, and this endpoint is not public.
      So only services (and api-gateway) can request api-key validation.
- auth providers
- security filter chain configuration

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

# running

## requirements

I tried to make it as easy to run as possible. You'll need

- java-21 (for unix systems I recommend https://sdkman.io)
- mongodb
- redis

If you run mongodb and redis with default settings (i.e. without custom
configuration), everything should work out of the box.

## build system

## IDE

## running from CLI

## running from IDE

## building and running jars

## initial setup

Initial data is set up only for SA (super-admin) tenant and user. SA user does
not have initial password set up, so as the first step you need to reset
password for SA user.

# tests

Functional tests are located in `test` folder. They are supposed to be executed
against a fully running system.

## pre-requisites

Tests always create all the data from scratch - tenants, users, etc. In order to
do so, tests use SA user (that's the only standard built-in user that can
create new tenants). Hence, to run tests, you need to reset password for SA
and export it as an environment variable `SA_PASSWORD` for test process.

For resetting password use:

```shell

```
