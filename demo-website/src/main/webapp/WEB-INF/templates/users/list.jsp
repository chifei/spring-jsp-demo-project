<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="j" uri="https://jweb.app/jsp/tlds/tags" %>

<!DOCTYPE html>
<html lang="${lang}">
<head>
    <title><spring:message code="user.user"/> - Spring JSP Demo</title>
    <link rel="stylesheet" href="/static/css/theme.css">
    <link rel="stylesheet" href="/static/css/kendo.default-v2.min.css">
    <link rel="stylesheet" href="/static/css/kendo.default.mobile.min.css">
    <link rel="stylesheet" href="/static/css/kendo.bootstrap-v4.min.css">
    <link rel="stylesheet" href="/static/css/header.css">
</head>
<jsp:include page="../fragments/header.jsp"/>
<body>
<div class="container">
    <c:if test="${not empty msg}">
        <div class="alert alert-${css} alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <strong>${msg}</strong>
        </div>
    </c:if>
    <j:hasPermission permission="user.write">
        <div class="themechooser" data-role="details">
            <a href="/admin/user/user/create" class="k-link k-button k-primary tc-activator"><spring:message code="user.createUser"/></a>
        </div>
    </j:hasPermission>
    <h1 id="exampleTitle">
        <strong><spring:message code="user.user"/></strong>
    </h1>

    <div id="exampleWrap">
        <div id="grid">
        </div>
    </div>

</div>
<jsp:include page="../fragments/footer.jsp"/>
<script type="text/javascript">
    window.user = ${userScript};
    window.messages = ${messageScript};
</script>
<script type="text/javascript" src="/static/js/page.js"></script>
<script type="text/javascript" src="/static/js/kendo.messages.${lang}.min.js"></script>
<script type="text/javascript" src="/static/js/user-list.js"></script>
</body>
</html>