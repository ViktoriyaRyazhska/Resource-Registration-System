<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-default" id="menubaradmin">
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

        <li><a href="<c:url value='/administrator/settings'/>">
            <spring:message code="label.admin.settings" />
        </a></li>
        <li><a
          href="<c:url value='/administrator/communities/show-all-communities'/>"><spring:message
              code="label.community.showall" /></a></li>
        <li><a href="<c:url value='/manualregistration'/>"><spring:message
              code="label.manualregister" /></a></li>

        <li><a href="#" id="unlockUsers"><spring:message
              code="label.security.unblockall" /></a></li>
              
<%--         <li><a href="<c:url value='/administrator/users/search'/>" --%>
<!--           class="glyphicon glyphicon-search"></a></li> -->
          
      </ul>
    </div>
  </div>
</nav>


<script>
        $("#unlockUsers").click(function(e){
            e.preventDefault();
             $.get("${pageContext.request.contextPath}/administrator/users/unlockusers", function(){
                });
             bootbox.alert(jQuery.i18n.prop('msg.unblockallusers'));
        });
</script>

<sec:authorize access="hasRole('ROLE_ADMIN')">
  <script type="application/javascript">
    //TODO RG change this to get id from server or using Spring WebSocket capability
    sessionID = '${pageContext.session.id}';
  </script>
  <script src="<c:url value='/resource/js/webSocketMessaging.js'/>"></script>
  <%--<link rel="stylesheet" href="<c:url value='/resource/css/messages.css'/>">--%>
  <style>
    .close-popup {
      background-image: url("<c:url value='/resource/img/close.svg'/>");
    }
  </style>
</sec:authorize>
