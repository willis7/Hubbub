package com.grailsinaction

class UserController {
    static scaffold = true

    def search() {}

    def results(String loginId) {
        def users = User.where {
            loginId =~ "%${loginId}"    // left hand side is the property of the User. Right hand side is the actions arg.
                                        // we add the SQL wildcard (%) so the user doesnt have to.
                                        // =~ is the equivalent to ILIKE in SQL
        }.list()
        return [users     : users,
                term      : params.loginId,
                totalUsers: User.count]
    }

    def register() {
        if (request.method == "POST") {
            def user = new User(params)
            if (user.validate()) {
                user.save()
                flash.message = "Successfully Created User"
                redirect(uri: '/')
            } else {
                flash.message = "Error Registering User"
                return [user: user]
            }
        }
    }
}
