<%--
  Created by IntelliJ IDEA.
  User: willis7
  Date: 08/12/14
  Time: 20:36
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Search Hubbub</title>
    <meta name="layout" content="main"/>
</head>

<body>
    <formset>
        <legend>Search for Friends</legend>
        <g:form action="results">
            <label for="loginId">Login ID</label>
            <g:textField name="loginId"/>
            <g:submitButton name="search" value="Search"/>
        </g:form>
    </formset>
</body>
</html>