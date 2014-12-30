package com.grailsinaction

import grails.transaction.Transactional

/**
 * PostService
 * @throws PostException
 */
@Transactional
class PostService {

    /**
     * Finds a user based on the id param. Binds data to new Post object and links new post to existing user.
     * Returns false if validation failure. Informs user of success or failure.
     *
     * @param loginId
     * @param content
     * @throws PostException    if validation fails
     * @return
     */
    def createPost(String loginId, String content) {
        def user = User.findByLoginId(loginId)
        if (user) {
            def post = new Post(content: content)
            user.addToPosts(post)
            if (post.validate() && user.save()) {
                return post
            } else {
                throw new PostException(message: 'Invalid or empty post', post: post)
            }
        }
        throw new PostException(message: 'Invalid User Id')
    }
}

/**
 * Forces transaction to rollback if exceptions occur.
 */
@Transactional
class PostException extends RuntimeException {
    String message
    Post post
}