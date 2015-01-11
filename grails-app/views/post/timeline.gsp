<%--
  Created by IntelliJ IDEA.
  User: willis7
  Date: 14/12/14
  Time: 15:36
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Timeline for ${user.profile ? user.profile.fullName : user.loginId}</title>
    <meta name="layout" content="main"/>
    <g:if test="${user.profile?.skin}">
        <asset:stylesheet src="${user.profile.skin}.css"/>
    </g:if>
</head>

<body>
<h1>Timeline for ${user.profile ? user.profile.fullName : user.loginId}</h1>

<g:if test="${flash.message}">
    <div class="flash">
        ${flash.message}
    </div>
</g:if>

<div id="newPost">
    <h3>What is ${user.profile.fullName} hacking on right now?</h3>

    <p>
        <g:form action="addPost" id="${params.id}">
            <g:textArea id="postContent" name="content" rows="3" cols="50"/><br/>
            <g:submitButton name="post" value="Post"/>
        </g:form>
    </p>
</div>

<div id="allPosts">
    <g:render template="postEntry" collection="${user.posts}" var="post"/>
</div>
</body>
</html>