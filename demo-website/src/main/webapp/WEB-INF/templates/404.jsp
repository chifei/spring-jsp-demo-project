<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8"/>
    <title>404 - Spring JSP demo</title>
    <link rel="stylesheet" href="/static/css/kendo.common.min.css">
    <link rel="stylesheet" href="/static/css/kendo.default.min.css">
    <link rel="stylesheet" href="/static/css/kendo.default.mobile.min.css">
    <link rel="stylesheet" href="/static/css/kendo.bootstrap-v4.min.css">
    <link rel="stylesheet" href="/static/css/theme.css">
    <link rel="stylesheet" href="/static/css/login.css">
</head>
<body>
<div class="container">
    <h1>404</h1>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>