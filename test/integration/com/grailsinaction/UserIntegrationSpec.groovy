package com.grailsinaction

import grails.test.spock.IntegrationSpec

class UserIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    def "Saving our first user to the database"() {
        given: "A brand new user"
        def joe = new User(loginId: 'joe', password: 'secret')

        when: "the user is saved"
        joe.save()

        then: "it saved successfully and can be found in the database"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId
    }

    def "Updating a saved user changes its properties"() {
        given: "An existing user"
        def existingUser = new User(loginId: 'joe', password: 'secret')
        existingUser.save(failonerror: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)
        foundUser.password = 'seasame'
        foundUser.save(failonerror: true)

        then: "The change is reflected in the database"
        User.get(existingUser.id).password == 'seasame'
    }

    def "Deleting an existing user removes it from the database"() {
        given: "An existing user"
        def user = new User(loginId: 'joe', password: 'secret')
        user.save(failonerror: true)

        when: "The user is deleted"
        def foundUser = User.get(user.id)
        foundUser.delete(flush: true)

        then: "The user is removed from the database"
        !User.exists(foundUser.id)
    }

    def "Saving a user with invalid properties causes an error"() {
        given: "A user which fails several field validations"
        def user = new User(loginId: 'joe', password: 'tiny')

        when: "The user is validated"
        user.validate()

        then:
        user.hasErrors()

        "size.toosmall" == user.errors.getFieldError("password").code
        "tiny" == user.errors.getFieldError("password").rejectedValue
        !user.errors.getFieldError("loginId")
    }

    def "Recovering from a failed save by fixing invalid properties"() {
        given: "A user that has invalid properties"
        def chuck = new User(loginId: 'chuck', password: 'tiny')
        assert chuck.save() == null
        assert chuck.hasErrors()

        when: "We fix the invalid properties"
        chuck.password = "fistfist"
        chuck.validate()

        then: "The user saves and validates fine"
        !chuck.hasErrors()
        chuck.save()
    }

    def "Ensure a user can follow other users"() {
        given: "A set of baseline users"
        def joe = new User(loginId: 'joe', password: 'password').save()
        def jane = new User(loginId: 'jane', password: 'password').save()
        def jill = new User(loginId: 'jill', password: 'password').save()

        when: "Joe follows Jane & Jill, and Jill follows Jane"
        joe.addToFollowing(jane)
        joe.addToFollowing(jill)
        jill.addToFollowing(jane)

        then: "Follower counts should match following people"
        2 == joe.following.size()
        1 == jill.following.size()
    }
}
