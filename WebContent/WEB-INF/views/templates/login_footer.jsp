<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="year" value="${now}" pattern="yyyy" />
<div style="margin-top: 10px;">
  <p>
    <span style="text-align: left; left: 30px">&copy;${year} <spring:message
        code="label.copyright"></spring:message></span>
  </p>
  <p>
    <span style="text-align: right; right: 30px"><a
      href="${pageContext.request.userPrincipal.name}/faq">Допомога
    </a> <a href="${pageContext.request.userPrincipal.name}/help">
        Зворотній зв'язок</a></span>
  </p>
</div>