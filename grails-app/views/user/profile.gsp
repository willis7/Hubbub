<%--
  Created by IntelliJ IDEA.
  User: willis7
  Date: 30/12/14
  Time: 16:33

  This GSP page creates a link back to /image/renderImage/<id> based on the loginId of the current user.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Profile for ${profile.fullName}</title>
    <meta name="layout" content="main">
    <style>
    .profilePic {
        border: 1px dotted gray;
        background: lightyellow;
        padding: 1em;
        font-size: 1em;
    }
    </style>
</head>

<body>
<div class="profilePic">
    <g:if test="${profile.photo}">
        <img src="${createLink(controller: 'image', action: 'renderImage', id: profile.user.loginId)}">
    </g:if>
    <p>Profile for <strong>${profile.fullName}</strong></p>

    <p>Bio: ${profile.bio}</p>
</div>
</body>
</html>