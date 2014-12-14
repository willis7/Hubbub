package com.grailsinaction

class PostController {
    static scaffold = true

    def timeline() {
        def user = User.findByLoginId(params.id)
        if (!user) {
            response.sendError(404)
        } else {
            return [user: user] // actions results placed into a map and returned to the view
        }
    }

    def addPost() {
        def user = User.findByLoginId(params.id)
        if (user) {
            def post = new Post(params) // params.content will be mapped to post.content. This is managed by Grails.
            user.addToPosts(post)
            if (user.save()) {
                flash.message = "Successfully created post"
            } else {
                flash.message = "Invalid or empty post"
            }
        } else {
            flash.message = "Invalid user id"
        }
        redirect(action: 'timeline', id: params.id)
    }
}
