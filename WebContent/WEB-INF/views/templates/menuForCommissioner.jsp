<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<nav class="navbar navbar-default" id="menubarcom">
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
          
            <li class="dropdown"><a href="#" class="dropdown-toggle"
            data-toggle="dropdown"><spring:message
              code="label.user.pagename" /><b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a
                href="<c:url value='/administrator/users/get-all-users'/>">
                  <spring:message
                    code="label.registrated.pagename.ACTIVE" />
              </a></li>
              <li><a
                href="<c:url value='/administrator/users/get-all-users?statusType=inactive'/>">
                  <spring:message
                    code="label.registrated.pagename.INACTIVE" />
              </a></li>
              <li><a
                href="<c:url value='/administrator/users/get-all-users?statusType=notcomfirmed'/>">
                  <spring:message
                    code="label.registrated.pagename.NOTCOMFIRMED" />
              </a></li>
              <li><a
                href="<c:url value='/administrator/users/get-all-users?statusType=block'/>">
                  <spring:message
                    code="label.registrated.pagename.BLOCK" />
              </a></li>

            </ul>
            </li>

            <c:if
              test="${(registrationMethod eq ('MANUAL')) || (registrationMethod eq ('MIXED'))}">
              <li><a href="<c:url value='/manualregistration'/>"><spring:message
                    code="label.manualregister" /></a></li>
            </c:if>

      </ul>
    </div>
  </div>
</nav>