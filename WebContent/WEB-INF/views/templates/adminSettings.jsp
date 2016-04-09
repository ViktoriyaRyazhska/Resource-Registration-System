<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container">
  <form:form id="сhangeReg" method="post" modelAttribute="settings"
    action="settings">
    <c:if test="${settings.success}">
      <div class="alert alert-success">
        <span><spring:message code="label.admin.settings.save"/></span>
      </div>
    </c:if>
    <h4>
      <spring:message code="label.admin.settings.method" />
    </h4>
    <p>
      <spring:message code="label.admin.settings.chooseoption" />
    </p>
    <div class="radio">
      <label><input type="radio" name="registrationMethod"
        value="PERSONAL"
        <c:if test="${settings.registrationMethod  == 'PERSONAL'}"> checked="checked" </c:if> />
        <spring:message code="label.admin.settings.personal" /> </label>
    </div>

    <div class="radio">
      <label><input type="radio" name="registrationMethod" value="MANUAL"
        <c:if test="${ settings.registrationMethod  == 'MANUAL'}">checked="checked"</c:if> />
        <spring:message code="label.admin.settings.manual" /> </label>
    </div>
    <div class="radio">
      <label><input type="radio" name="registrationMethod" value="MIXED"
        <c:if test="${ settings.registrationMethod  == 'MIXED'}"> checked="checked" </c:if> />
        <spring:message code="label.admin.settings.mixed" /> </label>
    </div>
    <div class="container">
      <div class="row">
        <h4>
          <spring:message code="label.admin.settings.timeZone" />
        </h4>
        <p>
          <spring:message code="label.admin.settings.timeZoneInput" />
        </p>
      </div>
      <div class="row control-group">
        <div class="col-md-3">
          <input id = "time_id" class="form-control col-md-4" name="timeZone"
                 value="${settings.timeZone}">
          <%--<form:errors path="timeZone" cssClass="error"--%>
                       <%--style="color:red" />--%>
          <c:if test="${settings.error}">
            <span class="error" style="color:red"><spring:message code="msg.settings.timeZone.error"/></span>
          </c:if>
          </div>
        </div>
      </div>

      <%--workaround to add vertical blank space--%>
      <div class="row">
        <h4></h4>
      </div>
    </div>
    <input type="submit" id="confirmRegistrationMethod"
      value="<spring:message code="label.admin.settings.confirm" />"
      class="btn btn-primary" />
  </form:form>
</div>

<link rel="stylesheet" type="text/css"
      href="<c:url value='/resource/css/suggestion.css'/>">

<%--<script src="<c:url value='/resource/js/adminSettings.js'/>"></script>--%>
<script src="<c:url value='/resource/js/lib/jquery.autocomplete.min.js'/>"></script>
<script src="<c:url value='/resource/js/timeZoneSuggestions.js'/>"></script>