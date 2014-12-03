package com.grailsinaction

class Post {
    String content
    Date dateCreated

    static belongsTo = [ user: User ]

    static constraints = {
        content blank: false
    }
}
