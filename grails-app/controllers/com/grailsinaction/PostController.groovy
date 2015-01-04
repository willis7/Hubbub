package com.grailsinaction

/**
 * PostController
 * Injects PostService instance into controller
 *
 * @see PostService
 */
class PostController {
    static scaffold = true
    static defaultAction = "home"

    def postService

    def home() {
        if (!params.id) {
            params.id = 'chuck_norris'
        }
        redirect(action: 'timeline', params: params)
    }

    /**
     * Retrieves user based on id parameter. Sends error code for nonexistent users.
     *
     * @param id
     * @return user    actions passes matched user to view as a map
     */
    def timeline(String id) {
        def user = User.findByLoginId(id)
        if (!user) {
            response.sendError(404)
        } else {
            return [user: user]
        }
    }

    /**
     * Invokes service method
     *
     * @param id
     * @param content
     * @return returns     user to timeline
     * @see PostService
     */
    def addPost(String id, String content) {
        try {
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: id)
    }

    /**
     * Displays the current users timeline or redirects to the login page if no user is logged in.
     */
    def personal() {
        if (!session.user) {
            redirect controller: "login", action: "form"
            return
        } else {
            // Need to reattach the user domain object to the sessin using
            // the refresh() method
            render view: "timeline", model: [user: session.user.refresh()]
        }
    }
}
