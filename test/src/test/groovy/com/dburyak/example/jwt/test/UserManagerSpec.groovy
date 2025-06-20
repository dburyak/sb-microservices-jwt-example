package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import com.dburyak.example.jwt.api.common.ExternalId
import com.dburyak.example.jwt.api.user.ContactInfo
import com.dburyak.example.jwt.api.user.UserWithRoles

class UserManagerSpec extends UserManagerLoggedInSpec {

    String username = "test-user-$tenantName"
    String email = "$username@test.jwt.example.dburyak.com"
    String password = rndString.nextGraph(25)

    def 'create regular user'() {
        given: 'a user-manager user is logged in'

        when: 'create a regular user'
        def newUser = userServiceClientUM.createByManager(UserWithRoles.builder()
                .externalId(new ExternalId(email))
                .username(username)
                .password(password)
                .displayName(username)
                .profileIcon('user')
                .contactInfo(new ContactInfo(email))
                .roles(Set.of('usr'))
                .build())

        then: 'the user is created successfully'
        newUser.username == username

        and: 'new user is found by username'
        def foundUser = userServiceClientUM.findByUsernameByManager(username)
        foundUser.username == username

        when: 'new user logs in'
        def loginResp = authServiceClientUM.jwtLogin(JwtLoginRequest.builder()
                .username(username)
                .password(password)
                .deviceId(umDeviceId)
                .build())

        then: 'the user is logged in successfully'
        loginResp.accessToken
        loginResp.userUuid == foundUser.uuid

        cleanup: 'delete the user'
        userServiceClientUM.deleteByUsernameByManager(username)
    }
}
