package com.grailsinaction

class UserController {
    static scaffold = true

    def search() {}

    /**
     * Action for the "search" form page. Takes an argument that matches the name on the text field in the form
     * then queries the DB for all users with a loginId that's like the search string.
     *
     * @param loginId text field in the form
     * @return
     */
    def results(String loginId) {
        def users = User.where {
            loginId =~ "%${loginId}"
            // left hand side is the property of the User. Right hand side is the actions arg.
            // we add the SQL wildcard (%) so the user doesnt have to.
            // =~ is the equivalent to ILIKE in SQL
        }.list()
        return [users     : users,
                term      : params.loginId,
                totalUsers: User.count]
    }

    /**
     * Action for registering a new user. Form submitted as POST, so create new user.
     */
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

    /**
     * Binds data from params to command object
     */
    def register2(UserRegistrationCommand urc) {
        // Uses hasErrors to check validations
        if (urc.hasErrors()) {
            render view: "register", model: [user: urc]
        } else {
            // Binds data to new user object
            def user = new User(urc.properties)
            user.profile = new Profile(urc.properties)

            // Saves and validates new user
            // You have to confirm the save() is successful, because the constraints only make sense in the domain class
            // and not in the command object.
            if (user.validate() && user.save()) {
                flash.message = "Welcome aboard, ${urc.fullName ?: urc.loginId}"
                redirect(uri: '/')
            } else {
                // maybe not unique loginId
                return [user: urc]
            }
        }
    }
}

class UserRegistrationCommand {
    String loginId
    String password
    String passwordRepeat
    byte[] photo
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress

    static constraints = {
        importFrom Profile
        importFrom User
        password(size: 6..8, blank: false, validator: { passwd, urc -> return passwd != urc.loginId })
        passwordRepeat(nullable: false, validator: { passwd2, urc -> return passwd2 == urc.password })
    }
}