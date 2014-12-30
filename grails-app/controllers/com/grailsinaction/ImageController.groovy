package com.grailsinaction

/**
 * PhotoUploadCommand
 * Holds uploaded photo data.
 */
class PhotoUploadCommand {
    byte[] photo
    String loginId
}

class ImageController {

    def upload(PhotoUploadCommand puc) {
        def user = User.findByLoginId(puc.loginId)
        user.profile.photo = puc.photo
        redirect controller: "user", action: "profile", id: puc.loginId
    }

    /**
     * Pass through user list to the upload form
     * @return userList
     */
    def form() {
        [userList: User.list()]
    }

    /**
     * Sends image data to the browser.
     */
    def renderImage(String id) {
        def user = User.findByLoginId(id)
        if (user?.profile?.photo) {
            response.setContentLength(user.profile.photo.size())
            response.outputStream.write(user.profile.photo)
        } else {
            response.sendError(404)
        }
    }
}