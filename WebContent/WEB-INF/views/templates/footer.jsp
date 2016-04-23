<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="year" value="${now}" pattern="yyyy" />

<link rel="stylesheet" type="text/css" href="<c:url value='/resource/css/cssload.css'/>">

<div style="margin-top: 10px;">
  <p>
    <span>&copy;${year} <spring:message code="label.copyright"></spring:message></span>
  </p>
</div>

<div id="dark_bg">
  <div class="windows8">
    <div class="wBall" id="wBall_1">
      <div class="wInnerBall"></div>
    </div>
    <div class="wBall" id="wBall_2">
      <div class="wInnerBall"></div>
    </div>
    <div class="wBall" id="wBall_3">
      <div class="wInnerBall"></div>
    </div>
    <div class="wBall" id="wBall_4">
      <div class="wInnerBall"></div>
    </div>
    <div class="wBall" id="wBall_5">
      <div class="wInnerBall"></div>
    </div>
  </div>
</div>
