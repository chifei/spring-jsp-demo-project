<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="${lang}">

<head>
    <meta charset="UTF-8"/>
    <title><spring:message code="user.login"/> - Spring JSP demo</title>
    <link rel="stylesheet" href="/static/css/kendo.common.min.css">
    <link rel="stylesheet" href="/static/css/kendo.default.min.css">
    <link rel="stylesheet" href="/static/css/kendo.default.mobile.min.css">
    <link rel="stylesheet" href="/static/css/kendo.bootstrap-v4.min.css">
    <link rel="stylesheet" href="/static/css/theme.css">
    <link rel="stylesheet" href="/static/css/login.css">
</head>
<body>
<div class="login-wrap">
    <div class="login-panel">
        <div class="demo-section k-content">
            <form id="login-form">
                <ul class="fieldlist">
                    <li>
                        <label for="username" class="required"><spring:message code="user.username"/></label>
                        <input id="username" name="username" type="text" class="k-textbox" required data-msg="user name required"/>
                    <li>
                        <label for="password" class="required"><spring:message code="user.password"/></label>
                        <input id="password" name="password" type="password" class="k-textbox" min-length="6"/>
                    </li>
                    <li>
                        <button class="k-button k-primary"><spring:message code="user.login"/></button>
                    </li>
                    <li class="status">
                    </li>
                </ul>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/kendo.core.min.js"></script>
<script type="text/javascript" src="/static/js/kendo.validator.min.js"></script>
<script type="text/javascript" src="/static/js/login.js"></script>
</body>
</html>