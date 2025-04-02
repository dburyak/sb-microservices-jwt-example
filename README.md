# sb-microservices-jwt-example
Example project to demonstrate how JWT auth can be used in microservices with SpringBoot.

It's a made up video streaming platform with no any functionality implemented. The goal
is to simply demonstrate how JWT auth can be used in microservices project with scaling
perspective. Design is multi-tenant for more realistic example to see how auth tokens of
different tenants can be isolated from each other.

Additionally we'll set up api-key auth mechanism to allow external systems to integrate
with us. This is just to help to demonstrate how multiple auth mechanisms can coexist.

## Roles and permissions design

Several roles are defined that are placed in JWT claims:
 - sa - super-admin - admin with access to all operations across all tenants
 - adm - admin - admin with access to all operations across its tenant only
 - cmg - content-manager - role for managing content
 - umg - user-manager - role for managing users
 - usr - user - regular user (watch movies) role

Each microservice will interpret those roles as a set of more fine-grained
permissions specialized for this particular service.

## spring-security

We have basically three key pieces of spring-security to make all this work:
 - filters - extract auth information from request
   -  
