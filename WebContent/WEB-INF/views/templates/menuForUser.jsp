<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<nav class="navbar navbar-default" id=menubaruser>
 <div class="container-fluid" id="navfluid">
         <div class="navbar-header">
         <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigationbar">
         <span class="sr-only">Toggle navigation</span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
        </button>
      </div>

    <div class="collapse navbar-collapse" id="navigationbar">
      <ul class="nav navbar-nav">
        <li><a href="<c:url value='/'/>"
          class="glyphicon glyphicon-home"></a></li>
        <li class="dropdown"><a
          href="<c:url value='/registrator/resource/searchOnMap'/>">
            <spring:message code="label.menu.resources" />
        </a></li>
        <li class="dropdown"><a href="#" class="dropdown-toggle"
            data-toggle="dropdown"><spring:message
              code="label.menu.inquiries" /><b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a
                    href="<c:url value='/inquiry/add/listInquiryUserInput'/>">
              <spring:message code="label.menu.inquiries.input" />
            </a></li>
            <li><a
              href="<c:url value='/inquiry/add/listInqUserOut'/>"> <spring:message
                  code="label.menu.inquiries.output" />
            </a></li>
          </ul></li>
      </ul>
    </div>
  </div>
</nav>