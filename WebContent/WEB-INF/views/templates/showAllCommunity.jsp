<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script src="<c:url value='/resource/js/deleteCommunity.js'/>"></script>
<script src="<c:url value='/resource/js/communityMembers.js'/>"></script>

<div style="text-align: center;">
  <h4>
    <spring:message code="label.community.pagename" />
  </h4>
</div>
<p class="pull-left">
  <a href="<c:url value='addCommunity'/>" class="btn btn-success"
    role="button"><spring:message code="label.community.add" /></a>
</p>
<div class="pull-right text-right">
  <label for="inactiveCheckbox"><spring:message code="label.community.showHidden"/></label>
  <input type="checkbox" id="inactiveCheckbox" style="position:relative;top:2px">
</div>
<table class="table table-striped table-bordered table-hover">

  <thead>
    <tr>
      <th style="width: 40%;"><spring:message
          code="label.community.name" /></th>
      <th style="width: 40%;"><spring:message
          code="label.community.titleNumber" /></th>
      <th style="text-align: center;"><spring:message
          code="label.restype.actions" /></th>
    </tr>
  </thead>

  <tbody>
    <c:if test="${not empty listOfTerritorialCommunity}">
      <c:forEach items="${listOfTerritorialCommunity}" var="commun">
        <tr class="commun" type="${commun.active}">
          <td><a href = "#" class = 'communName'>${commun.name}</a></td>
          <td>${commun.registrationNumber}</td>
          <td style="text-align: center; width: 100%;">
            <div
              style="display: inline-block; margin: 2px auto; width: 45%; min-width: 94px;">
              <a
                href="editCommunity?id=${commun.territorialCommunityId}"
                class="btn btn-primary" style="width: 100%;"
                id="editcommunity" role="button"><spring:message
                  code="label.community.edit" /> </a>
            </div> <c:choose>
              <c:when test="${commun.active eq 0}">
                <div
                  style="display: inline-block; margin: 2px auto; width: 45%; min-width: 94px;">
                  <a
                    href="activateCommunity/${commun.territorialCommunityId}"
                    class="btn btn-warning" style="width: 100%;"
                    id="activecommunity" role="button"><spring:message
                      code="label.community.activate" /> </a>
                </div>
              </c:when>
              <c:otherwise>
                    <div
                  style="display: inline-block; margin: 2px auto; width: 45%; min-width: 94px;">
                  <a
                    href="deleteCommunity/${commun.territorialCommunityId}"
                    class="btn btn-danger" style="width: 100%;"
                    id="deletecommunity" role="button"><spring:message
                      code="label.community.delete" /> </a>
                </div>
              </c:otherwise>
            </c:choose>

          </td>
        </tr>
      </c:forEach>
    </c:if>
  </tbody>
</table>