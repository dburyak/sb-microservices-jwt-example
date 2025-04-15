package com.dburyak.example.jwt.lib.auth;

import java.util.Set;

public interface AuthoritiesMapper {

    /**
     * Maps global publicly exposed (as claims in JWT) roles to more fine-grained Spring Security authorities.
     * Spring-security granted authorities are not exposed anywhere, they are used only in the code for access control.
     * Each microservice can have its own mapping. Having them separate allows evolving roles and authorities
     * independently.
     *
     * @param jwtRoles roles from JWT token
     *
     * @return spring-security authorities that will be used for configuring access for endpoints
     */
    Set<String> mapToAuthorities(Set<Role> jwtRoles);
}
