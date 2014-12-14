package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PostController)
@Mock([User, Post])
class PostControllerSpec extends Specification {

    def "Get a users timeline given their id"() {
        given: "A user with posts in the db"
        def chuck = new User(loginId: 'chuck_norris', password: 'password')
        chuck.addToPosts(new Post(content: '1st post'))
        chuck.addToPosts(new Post(content: '2nd post'))
        chuck.save(failOnError: true)

        and: "A loginId parameter"
        params.id = chuck.loginId

        when: "the timeline is invoked"
        def model = controller.timeline()

        then: "the user is in the returned model"
        model.user.loginId == 'chuck_norris'
        model.user.posts.size() == 2
    }

    def "Check that a non-existent user returns a 404"() {
        given: "the id of a non-existent user"
        params.id = "this-user-id-does-not-exist"

        when: "the timeline is invoked"
        controller.timeline()

        then: "a 404 is sent to the browser"
        response.status == 404
    }
}
