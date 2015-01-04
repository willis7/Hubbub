package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PostController)
@Mock([User, Post, LameSecurityFilters])
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

    def "Adding a valid new post to the timeline"() {
        given: "a mock post service"
        def mockPostService = Mock(PostService)
        1 * mockPostService.createPost(_, _) >> new Post(content: 'Mock Post')
        controller.postService = mockPostService

        when: "controller is invoked"
        def result = controller.addPost('joe_cool', 'Posting up a storm')

        then: "redirected to timeline, flash message tells us all is well"
        flash.message ==~ /Added new post: Mock.*/
        response.redirectedUrl == '/post/timeline/joe_cool'
    }

    @Unroll
    def "Testing id of #suppliedId redirects to #expectedUrl"() {
        given:
        params.id = suppliedId

        when: "Controller is invoked"
        controller.home()

        then:
        response.redirectedUrl == expectedUrl

        where:
        suppliedId | expectedUrl
        'joe_cool' | '/post/timeline/joe_cool'
        null       | '/post/timeline/chuck_norris'

    }

    def "Exercising security filter for an unauthenticated user"() {
        when:
        withFilters(action: "addPost") {
            controller.addPost("glenn_a_smith", "A first post")
        }

        then:
        response.redirectedUrl == '/login/form'
    }
}
