<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="j" uri="https://jweb.app/jsp/tlds/tags" %>

<div id="megaStore">
    <ul id="menu">
        <j:hasPermission permission="product.read">
            <li>
                <a href="/admin/product"><spring:message code="product.product"/></a>
            </li>
        </j:hasPermission>
        <j:hasPermission permission="user.read">
            <li>
                <spring:message code="user.user"/>
                <ul>
                    <li>
                        <a href="/admin/user/user"><spring:message code="user.user"/></a>
                    </li>
                    <li>
                        <a href="/admin/user/role"><spring:message code="user.role"/></a>
                    </li>
                </ul>
            </li>
        </j:hasPermission>
        <li>
            <spring:message code="language.language"/>
            <ul>
                <li>
                    <a href="${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}?lang=en-US"><spring:message code="language.en-US"/></a>
                </li>
                <li>
                    <a href="${pageContext.request.getAttribute("javax.servlet.forward.request_uri")}?lang=zh-CN"><spring:message code="language.zh-CN"/></a>
                </li>
            </ul>
        </li>
        <li>
            <a href="/admin/logout"><spring:message code="user.logout"/></a>
        </li>
    </ul>
</div>

<div id="loading-page" style="position:fixed;width: 100%;height: 100%;top: 0;left: 0;z-index: 1000;background:#fff"></div>