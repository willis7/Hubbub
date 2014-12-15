package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
@Mock([User, Profile])
class UserControllerSpec extends Specification {

    def "Registering a user with known good parameters"() {
        given: "a set of user parameters"
        params.with {
            loginId = "sion_a_williams"
            password = "winning"
            homepage = "https://github.com/willis7/Hubbub"
        }

        and: "a set of profile parameters"
        params['profile.fullName'] = "Sion Williams"
        params['profile.email'] = "sion@williams.com"
        params['profile.homepage'] = "https://github.com/willis7/Hubbub"

        when: "the user is registered"
        request.method = "POST"
        controller.register()

        then: "the user is created, and the browser redirected"
        response.redirectedUrl == '/'
        User.count() == 1
        Profile.count() == 1
    }
}
