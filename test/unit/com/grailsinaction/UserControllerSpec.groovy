package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.Use

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

    @Unroll
    def "Registration command object for #loginId validate correctly"() {
        given: "a mocked command object"
        def urc = mockCommandObject(UserRegistrationCommand)

        and: "a set of initial values from the spock test"
        urc.loginId = loginId
        urc.password = password
        urc.passwordRepeat = passwordRepeat
        urc.fullName = "Your name here"
        urc.email = "someone@nowhere.com"

        when: "the validator is invoked"
        def isValidRegistration = urc.validate()

        then: "the appropriate fields are flagged as errors"
        isValidRegistration == anticipatedValid
        urc.errors.getFieldError(fieldInError)?.code == errorCode

        where:
        loginId | password   | passwordRepeat | anticipatedValid | fieldInError     | errorCode
        "glen"  | "password" | "no-match"     | false            | "passwordRepeat" | "validator.invalid"
        "peter" | "password" | "password"     | true             | null             | null
        "a" | "password" | "password" | false | "loginId" | "size.toosmall"
    }

    def "Invoking the new register action via a command object"() {
        given: "a configured command object"
        def urc = mockCommandObject(UserRegistrationCommand)
        urc.with {
            loginId = "glen_a_smith"
            fullName = "Glen Smith"
            email = "glen@bytecode.com.au"
            password = "password"
            passwordRepeat = "password"
        }

        // You must call validate() manually when testing with controllers.
        and: "which has been validated"
        urc.validate()

        when: "the register action is invoked"
        // pass your command object into your controller
        controller.register2(urc)

        then: "the user is registered and browser redirected"
        !urc.hasErrors()
        response.redirectedUrl == '/'
        User.count() == 1
        Profile.count() == 1
    }
}
