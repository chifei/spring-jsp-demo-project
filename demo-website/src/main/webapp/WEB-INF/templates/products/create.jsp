<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="${lang}">
<head>
    <title>Create Product - Spring JSP Demo</title>
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
        <a class="k-link k-button k-primary tc-activator"><spring:message code="product.save"/></a>
    </div>
    <h1 id="exampleTitle">
        <strong><spring:message code="product.createProduct"/></strong>
    </h1>

    <div id="exampleWrap">
        <div class="demo-section k-content">
            <form>
                <ul class="fieldlist">
                    <li>
                        <label for="name"><spring:message code="product.name"/></label>
                        <input id="name" name="name" type="text" required class="k-textbox" style="width: 100%;"/>
                    </li>
                    <li>
                        <label for="description"><spring:message code="product.description"/></label>
                        <textarea id="description" name="description" required class="k-textbox" style="width: 100%;"></textarea>
                    </li>
                </ul>
            </form>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
<script type="text/javascript" src="/static/js/product-create.js"></script>
</body>
</html>