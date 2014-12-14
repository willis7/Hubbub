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
}
