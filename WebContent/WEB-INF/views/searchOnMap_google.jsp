<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<%-- styles --%> 
  <link rel="stylesheet" type="text/css"
    href="<c:url value='/resource/css/jquery.dataTables.min.css'/>">
  <link rel="stylesheet" type="text/css"
    href="<c:url value='/resource/css/responsive.dataTables.min.css'/>">
  <link rel="stylesheet" type="text/css"
    href="<c:url value='/resource/css/cssload.css'/>">
  <link rel="stylesheet" type="text/css"
    href="<c:url value='/resource/css/searchOnMap.css'/>">
<%-- scripts --%> 
  <script src="<c:url value='/resource/js/lib/jquery.dataTables.min.js'/>"></script>
  <script src="<c:url value='/resource/js/lib/dataTables.responsive.min.js'/>"></script>
  <script src="http://maps.googleapis.com/maps/api/js?libraries=drawing,places,geometry&sensor=false"></script>
  <script type="text/javascript" src="<c:url value='/resource/js/searchOnMap.js'/>"></script>

<div class="container"
  style="margin-bottom: 25px; line-height: 28px; vertical-align: top;">
  <h3>
        <spring:message code="label.menu.resources" />
  </h3>
  <div class="col-xs-12 controls">
    <div id="searchByPointButton"
      class="btn btn-primary toggle-button col-sm-3 col-xs-12">
        <spring:message code="label.search.byCoordinate" />
    </div> 
    <div id="searchByAreaButton"
      class="btn btn-primary toggle-button col-sm-3 col-xs-12">
        <spring:message code="label.search.byRegion" />
    </div> 
    <div id="searchByParameterButton"
      class="btn btn-primary toggle-button col-sm-3 col-xs-12">
        <spring:message code="label.search.byParameter" />
    </div>
  </div>
  <div class="clearfix"></div>
  
  <div id="searchByPointDiv" class="col-md-12 searchDiv">
    <p>
      <spring:message code="label.search.byCoordinate.massage" />
      :
    </p>
    <div class="col-lg-6 ">
        <fieldset>
          <legend>
            <label> 
              <spring:message code="label.resource.latitude" />:
            </label>
          </legend>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
              <div class="input-group">
               <input type="text" class="latitudeDegrees form-control"
                  placeholder="<spring:message code="label.resource.coordinates.degrees" />"
               >
              <span class="input-group-addon">°</span>
              </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
              <div class="input-group">
              <input type="text" class="latitudeMinutes form-control"
                  placeholder="<spring:message code="label.resource.coordinates.minutes" />"
               >
              <span class="input-group-addon">'</span>
              </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
              <div class="input-group">
                <input type="text" class="latitudeSeconds form-control"
                  placeholder="<spring:message code="label.resource.coordinates.seconds" />"
               >
              <span class="input-group-addon">"</span>
              </div>
          </div>
      </fieldset>
    </div>
    <div class="col-lg-6">
        <fieldset>
          <legend>
            <label> 
              <spring:message code="label.resource.longitude" />:
            </label>
          </legend>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
              <div class="input-group">
               <input type="text" class="longitudeDegrees form-control pikaso"
              placeholder="<spring:message code="label.resource.coordinates.degrees" />"
              >
              <span class="input-group-addon">°</span>
              </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
              <div class="input-group">
              <input type="text" class="longitudeMinutes form-control pikaso"
              placeholder="<spring:message
              code="label.resource.coordinates.minutes" />"
              >
              <span class="input-group-addon">'</span>
              </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
              <div class="input-group">
                <input type="text" class="longitudeSeconds form-control pikaso"
              placeholder="<spring:message
              code="label.resource.coordinates.seconds" />"
              >
              <span class="input-group-addon">"</span>
              </div>
          </div>
          </fieldset>
    </div>
    <%-- .top-margin is declared in system.css --%>
    <div class="col-xs-12 top-margin">
      <button id="searchOnMapButton" class="btn btn-success col-xs-12">
        <spring:message code="label.menu.search" />
      </button>
    </div>
  </div>
  
  
  <div id="searchByAreaDiv" class="col-md-12 searchDiv">
    <p>
      <spring:message code="label.search.byRegion.massage" />
      :
    </p>
    <%-- .bottom-line is declared in system.css --%>
    <h3 class="bottom-line"><spring:message code="label.search.coordinate" /> 1</h3>
    <div id="first_point">
      <div class="col-lg-6 ">
          <fieldset>
            <legend>
              <label> 
                <spring:message code="label.resource.latitude" />:
              </label>
            </legend>
            <%-- .top-margin is declared in system.css --%>
            <div class="col-lg-4 top-margin">
                <div class="input-group">
                 <input type="text" class="latitudeDegrees form-control"
                    placeholder="<spring:message code="label.resource.coordinates.degrees" />"
                 >
                <span class="input-group-addon">°</span>
                </div>
            </div>
            <%-- .top-margin is declared in system.css --%>
            <div class="col-lg-4 top-margin">
                <div class="input-group">
                <input type="text" class="latitudeMinutes form-control"
                    placeholder="<spring:message code="label.resource.coordinates.minutes" />"
                 >
                <span class="input-group-addon">'</span>
                </div>
            </div>
            <%-- .top-margin is declared in system.css --%>
            <div class="col-lg-4 top-margin">
                <div class="input-group">
                  <input type="text" class="latitudeSeconds form-control"
                    placeholder="<spring:message code="label.resource.coordinates.seconds" />"
                 >
                <span class="input-group-addon">"</span>
                </div>
            </div>
          </fieldset>
      </div>
      <div class="col-lg-6">
        <fieldset>
          <legend>
            <label> 
              <spring:message code="label.resource.longitude" />:
            </label>
          </legend>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
            <div class="input-group">
              <input type="text" class="longitudeDegrees form-control pikaso"
                  placeholder="<spring:message code="label.resource.coordinates.degrees" />"
              >
              <span class="input-group-addon">°</span>
            </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
            <div class="input-group">
              <input type="text" class="longitudeMinutes form-control pikaso"
                 placeholder="<spring:message
                 code="label.resource.coordinates.minutes" />"
              >
              <span class="input-group-addon">'</span>
            </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
            <div class="input-group">
              <input type="text" class="longitudeSeconds form-control pikaso"
                 placeholder="<spring:message
                 code="label.resource.coordinates.seconds" />"
              >
              <span class="input-group-addon">"</span>
            </div>
          </div>
        </fieldset>
      </div>
    </div>
    <%-- .bottom-line is declared in system.css --%>
    <h3 class="bottom-line"><spring:message code="label.search.coordinate" /> 2</h3>
    <div id="second_point">
      <div class="col-lg-6 ">
          <fieldset>
            <legend>
              <label> 
                <spring:message code="label.resource.latitude" />:
              </label>
            </legend>
            <%-- .top-margin is declared in system.css --%>
            <div class="col-lg-4 top-margin">
                <div class="input-group">
                 <input type="text" class="latitudeDegrees form-control"
                    placeholder="<spring:message code="label.resource.coordinates.degrees" />"
                 >
                <span class="input-group-addon">°</span>
                </div>
            </div>
            <%-- .top-margin is declared in system.css --%>
            <div class="col-lg-4 top-margin">
                <div class="input-group">
                <input type="text" class="latitudeMinutes form-control"
                    placeholder="<spring:message code="label.resource.coordinates.minutes" />"
                 >
                <span class="input-group-addon">'</span>
                </div>
            </div>
            <%-- .top-margin is declared in system.css --%>
            <div class="col-lg-4 top-margin">
                <div class="input-group">
                  <input type="text" class="latitudeSeconds form-control"
                    placeholder="<spring:message code="label.resource.coordinates.seconds" />"
                 >
                <span class="input-group-addon">"</span>
                </div>
            </div>
          </fieldset>
      </div>
      <div class="col-lg-6">
        <fieldset>
          <legend>
            <label> 
              <spring:message code="label.resource.longitude" />:
            </label>
          </legend>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
            <div class="input-group">
              <input type="text" class="longitudeDegrees form-control pikaso"
                  placeholder="<spring:message code="label.resource.coordinates.degrees" />"
              >
              <span class="input-group-addon">°</span>
            </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
            <div class="input-group">
              <input type="text" class="longitudeMinutes form-control pikaso"
                 placeholder="<spring:message
                 code="label.resource.coordinates.minutes" />"
              >
              <span class="input-group-addon">'</span>
            </div>
          </div>
          <%-- .top-margin is declared in system.css --%>
          <div class="col-lg-4 top-margin">
            <div class="input-group">
              <input type="text" class="longitudeSeconds form-control pikaso"
                 placeholder="<spring:message
                 code="label.resource.coordinates.seconds" />"
              >
              <span class="input-group-addon">"</span>
            </div>
          </div>
        </fieldset>
      </div>
    </div>
    <%-- .top-margin is declared in system.css --%>
    <div class="col-xs-12 top-margin">
      <button id="searchOnMapButton" class="btn btn-success col-xs-12">
        <spring:message code="label.menu.search" />
      </button>
    </div>
  </div>
  
  <div id="searchByParameterDiv" class="col-md-12 searchDiv">
    <c:if test="${not empty resourceTypes}">
      <div style="padding-bottom: 15px;">
        <label class="">
            <spring:message code="label.resource.selectType" />:
        </label> 
        <select id="resourcesTypeSelect" class="form-control"
            style="width: auto; display: inline">
          <c:forEach items="${resourceTypes}" var="resourceType">
            <option value="${resourceType.typeId}">${resourceType.typeName}</option>
          </c:forEach>
        </select>
      </div>
      <div id="searchParameters" class="container"></div>
    </c:if>
  </div>
</div>

<div class="container" id="searchResult" class="col-md-12"></div>
<div id="paginationDiv" class="col-md-12"></div>
<div id="resTypeFilter" class="col-md-12"></div>
<div id="map_canvas" class="container" style="height: 700px;"></div>

<%--Search field for Google Map--%>
<p>
  <input id="gmaps-input" class="controls gmap-input"
    style="width: 300px;" type="text"
    placeholder=<spring:message code="label.menu.searchOnMap"/>>
</p>