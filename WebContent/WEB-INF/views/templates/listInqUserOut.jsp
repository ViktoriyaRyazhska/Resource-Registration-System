<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>

<%-- styles --%> 
  <link rel="stylesheet" type="text/css"
      href="<c:url value='/resource/css/jquery.dataTables.min.css'/>">
  <link rel="stylesheet" type="text/css"
      href="<c:url value='/resource/css/responsive.dataTables.min.css'/>">
<%-- scripts --%> 
  <script src="<c:url value='/resource/js/lib/jquery.dataTables.min.js'/>"></script>
  <script src="<c:url value='/resource/js/lib/dataTables.responsive.min.js'/>"></script>
  <script src="<c:url value='/resource/js/deleteProcuration.js'/>"></script>

<div style="text-align: center;">
  <h4>
    <spring:message code="label.inquiry.listInquiryUserOut.pagename" />
  </h4>
</div>

<div style="width: 90%; margin: 0 auto;">

<table id="datatable" class="display">
  <thead>
    <tr>
      <th><spring:message code="label.inquiry.date" /></th>
      <th><spring:message code="label.inquiry.user" /></th>
      <sec:authorize access="hasRole('USER')">
        <th><spring:message code="label.resource.registrator" /></th>
      </sec:authorize>
      <th hidden="true" class="never"><spring:message
          code="label.inquiry.inquiryType" /></th>
      <th><spring:message code="label.resource.identifier" /></th>
      <th><spring:message code="label.restype.actions" /></th>
    </tr>
  </thead>

  <tbody>
    <c:if test="${not empty listInquiryUserOut}">
        
      <c:forEach items="${listInquiryUserOut}" var="inquiryUserOut">
        <tr>
          <fmt:formatDate value="${inquiryUserOut.date}"
            pattern="dd.MM.yyyy" var="Date" />
          <td>${Date}</td>
          <td>${inquiryUserOut.userName}</td>
          <sec:authorize access="hasRole('USER')">
            <td>${inquiryUserOut.registratorName}</td>
          </sec:authorize>
          <td hidden="true">${inquiryUserOut.inquiryType}</td>
          <td><a
            href="<c:url value='/registrator/resource/get?id=${inquiryUserOut.resourceIdentifier}' />">
              ${inquiryUserOut.resourceIdentifier} </a></td>
          <td>
            <%-- .nowrap is defined inside system.css --%>
            <div class="block nowrap">
              <sec:authorize access="hasRole('REGISTRATOR')">
                <%-- .inq-act is defined inside system.css --%>
                <a
                  href="<c:url value='/inquiry/add/delete/${inquiryUserOut.inquiryId}' />"
                  class="btn btn-danger inq-act" role="button"
                  id="deleteInquiry"> <spring:message
                    code="label.restype.delete" /></a> 
             </sec:authorize> 
             <%-- .inq-act is defined inside system.css --%>
             <a
                href="<c:url value='/inquiry/add/printOutput/${inquiryUserOut.inquiryId}' />"
                class="btn btn-primary inq-act" role="button" target="_blank" > <spring:message
                   code="label.inquiry.print" /></a>
             <sec:authorize access="hasRole('REGISTRATOR')">
                <%-- .inq-act is defined inside system.css --%>
                <a
                  href="<c:url value='/inquiry/add/printExtract/${inquiryUserOut.inquiryId}' />"
                  class="btn btn-primary inq-act" role="button" target="_blank" > <spring:message
                     code="label.inquiry.printExtract" /></a>
             </sec:authorize> 

            </div>
          </td>
        </tr>
      </c:forEach>
    </c:if>
  </tbody>
</table>

</div>

<div style="clear:both"></div>

<script type="text/javascript">
jQuery(document).ready(function($) {
  table = $('#datatable').DataTable({
    "responsive": true,
    "ordering": false,
    "bAutoWidth": false,
    "bSortCellsTop": true,
    "columnDefs": [
        { responsivePriority: 1, targets: 0 }, 
        { responsivePriority: 2, targets: -2 } // object registration number
    ]
  });
  
  table.on( 'responsive-display', function ( e, datatable, row, showHide, update ) {
    if($('.dtr-data .nowrap').length>0){
      $('.dtr-data .nowrap').removeClass('nowrap');
      $('.inq-act').addClass('detail-view');
    } else {
      $('.inq-act').removeClass('detail-view');
    }
  });

});
</script>

