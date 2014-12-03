package com.grailsinaction

import grails.test.spock.IntegrationSpec

class PostIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "Adding posts to user links post to user"() {
        given: "A brand new user"
        def user = new User(loginId: 'joe', password: 'secret')
        user.save(failOnError: true)

        when: "Several posts are added to the user"
        user.addToPosts(new Post(content: 'First post... W00T'))
        user.addToPosts(new Post(content: 'Second post ...'))
        user.addToPosts(new Post(content: 'Third post ...'))

        then: "The user has a list of posts attached"
        3 == User.get(user.id).posts.size()
    }
}
