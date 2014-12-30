package com.grailsinaction

import grails.test.spock.IntegrationSpec

/**
 * All of these queries require the database to be pre-populated. This is being managed by bootStrap.groovy
 */
class QueryIntegrationSpec extends IntegrationSpec {

    def "Simple property comparison"() {
        when: "Users are selected by a simple password match"
        def users = User.where {
            password == "testing"
        }.list(sort: "loginId")

        then: "The users with the same password are returned"
        users*.loginId == ['frankie']
    }

    def "Multiple criteria"() {
        when: "A user is selected by loginId or password"
        def users = User.where {
            // Combines conditions with logical operators
            loginId == 'frankie' || password == 'crikey'
        }.list(sort: 'loginId')

        then: "The matching loginIds are returned"
        users*.loginId == ['dillon', 'frankie', 'sara']
    }

    def "Query on association"() {
        when: "The 'following' collection is queried"
        def users = User.where {
            // Queries on single and multi-ended associations via standard "." syntax
            following.loginId == 'sara'
        }.list(sort: 'loginId')

        then: "A list of followers of the given user is returned"
        users*.loginId == ['phil']
    }

    def "Query against a range value"() {
        given: "The current date and time"
        def now = new Date()

        when: "The 'dateCreated' property is queried"
        def users = User.where {
            // Uses in operator plus a range to do a SQL BETWEEN query
            dateCreated in (now.minus(1))..now
        }.list(sort: 'loginId', order: 'desc')

        then: "The users created in the range are returned"
        users*.loginId == ['phil', 'peter', 'glen', 'frankie', 'chuck_norris', 'admin']
    }

    def "Retrieve a single instance"() {
        when: "A specific user is queried with get()"
        def user = User.where {
            loginId == 'phil'
            // Returns a single instance rather than list via get(). Throws an exception if there's more than one
            // matching result.
        }.get()

        then: "A single instance is returned"
        user.password == "thomas"
    }
}
