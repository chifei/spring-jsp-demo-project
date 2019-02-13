<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><spring:message code="product.product"/> - Spring JSP Demo</title>
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
    <div class="themechooser" data-role="details">
        <form action="" method="GET" class="search-form">
            <input id="name" name="name" type="text" required class="k-textbox" value="${param.name}" style="width: 100%;"/>
            <button type="submit" class="k-link k-button k-primary tc-activator"><spring:message code="product.search"/></button>
        </form>

        <j:hasPermission permission="product.write">
            <a href="/admin/product/create" class="k-link k-button k-primary tc-activator"><spring:message code="product.createProduct"/></a>
            <label for="files" class="k-link k-button tc-activator">
                <spring:message code="product.upload"/>
                <input name="file" id="files" type="file"/>
            </label>
            <a href="/admin/product/export?name=${param.name}" class="k-link k-button tc-activator"><spring:message code="product.export"/></a>
            <a href="/admin/product/template/download" class="k-link k-button tc-activator"><spring:message code="product.templateDownload"/></a>
        </j:hasPermission>
    </div>
    <h1 id="exampleTitle">
        <strong><spring:message code="product.product"/></strong>
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
<script type="text/javascript" src="/static/js/product-list.js"></script>
</body>
</html>