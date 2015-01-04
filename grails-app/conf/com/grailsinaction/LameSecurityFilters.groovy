package com.grailsinaction

/**
 * LameSecurityFilters
 * Intercept requests and ensures user has access to target resource.
 */
class LameSecurityFilters {

    def filters = {
        // Names security rules and limits filter to two actions
        secureActions(controller: 'post', action: '(addPost|deletePost)') {
            before = {
                // Tests for presence of impersonateId param
                if (params.impersonateId) {
                    session.user = User.findByLoginId(params.impersonateId)
                }
                // Tests for existing user in session
                if (!session.user) {
                    redirect(controller: 'login', action: 'form')
                    // Stops subsequent filter from firing
                    return false
                }
            }
            after = { Map model ->
            }
            afterView = { Exception e ->
                // Logs diagnostic data after view completes
                log.debug "Finished running ${controllerName} - ${actionName}"
            }
        }
    }
}
