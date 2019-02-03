<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="${lang}">
<head>
    <title><spring:message code="product.view"/> - Spring JSP Demo</title>
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
    <h1 id="exampleTitle">
        <strong><spring:message code="product.view"/></strong>
    </h1>

    <div id="exampleWrap">
        <div class="demo-section k-content">
            <form>
                <ul class="fieldlist">
                    <li>
                        <label><spring:message code="product.name"/></label>
                        <p>${product.id}</p>
                    </li>
                    <li>
                        <label><spring:message code="product.name"/></label>
                        <p>${product.name}</p>
                    </li>
                    <li>
                        <label><spring:message code="product.description"/></label>
                        <p>${product.description}</p>
                    </li>
                </ul>
            </form>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
<script type="text/javascript" src="/static/js/product-update.js"></script>
</body>
</html>