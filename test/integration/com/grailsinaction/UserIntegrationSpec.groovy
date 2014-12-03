package com.grailsinaction

import grails.test.spock.IntegrationSpec

class UserIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    def "Saving our first user to the database"() {
        given: "A brand new user"
        def joe = new User(loginId: 'joe', password: 'secret', homepage: 'http://www.grailsinaction.com')

        when: "the user is saved"
        joe.save()

        then: "it saved successfully and can be found in the database"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId
    }

    def "Updating a saved user changes its properties"() {
        given: "An existing user"
        def existingUser = new User(loginId: 'joe', password: 'secret', homepage: 'http://www.grailsinaction.com')
        existingUser.save(failonerror: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)
        foundUser.password = 'seasame'
        foundUser.save(failonerror: true)

        then: "The change is reflected in the database"
        User.get(existingUser.id).password == 'seasame'
    }
}
