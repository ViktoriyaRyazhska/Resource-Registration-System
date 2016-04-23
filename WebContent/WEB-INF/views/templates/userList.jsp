<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<link rel="stylesheet" type="text/css"
 href="<c:url value='/resource/css/suggestion.css'/>">
<link rel="stylesheet" type="text/css"
 href="<c:url value='/resource/css/cssload.css'/>">
  <link rel="stylesheet" type="text/css"
 href="https://cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css">
 <link rel="stylesheet" type="text/css"
 href="https://cdn.datatables.net/responsive/2.0.2/css/responsive.dataTables.min.css">
<script src="<c:url value='/resource/js/lib/jquery.autocomplete.min.js'/>"></script>
 <script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
 <script src="https://cdn.datatables.net/responsive/2.0.2/js/dataTables.responsive.min.js"></script>
 <script src="<c:url value='/resource/js/asearch_row.js'/>"></script>


<c:if test="${not empty tableSetting.script}">
 <script src="<c:url value='/resource/js/${tableSetting.script}.js'/>"></script>
</c:if>
<div class="container">

 <c:if test="${not empty tableSetting.tableTitle}">

  <div style="text-align: center;">
   <h4>
    <spring:message code="label.registrated.pagename.${statusType}" />
    <spring:message code="label.registrated.pagename.users" />
   </h4>
  </div>

  <div class="dataTable_wrapper" style="overflow:hidden;">
   <tiles:insertAttribute name="baseActionsMenu" />

   <table id="example"
    class="table table-striped table-bordered table-hover" width="100%">
    <thead>
     <tr>
      <c:forEach items="${tableSetting.columns}" var="entry">
       <th><spring:message code="${entry.value.title}" /></th>
      </c:forEach>
     </tr>
    </thead>
    <tfoot style="display: table-header-group">
     <tr>
      <c:forEach items="${tableSetting.columns}" var="entry"
       varStatus="status">
       <c:if test="${entry.value.type eq 'search'}">
        <th class="search_element">
         <div class="form-group">
          <div class="col-md-12" style="padding: 0">
           <input type="hidden" id="searchTypeIndex${entry.key}"
            name="category" value="like" /> <input maxlength="50"
            id="inputIndex${entry.key}" class="form-control" type="text"
            placeholder="<spring:message code="${entry.value.title}" />" />
          </div>
         </div>
        </th>
       </c:if>
       <c:if test="${entry.value.type eq 'status'}">
        <th class="search_element">
         <div class="form-group">
          <div class="col-md-12" style="padding: 0">
           <input type="hidden" id="searchTypeIndex${entry.key}"
            name="category" value="statusType" /> 
            
            <div id="toggle_dt">
  Click here
</div>
            
            <input maxlength="50"
            id="inputIndex${entry.key}" class="form-control"
            type="hidden" value="${statusType}" />
          </div>
         </div>
        </th>
       </c:if>
       <c:if test="${entry.value.type eq 'role'}">
        <th class="search_element">
         <div class="form-group">
            <input type="hidden" id="searchTypeIndex${entry.key}"
            name="category" value="roleType" /> 
            
            
           <select class="form-control col-sm-3" name="role_type" id="inputIndex${entry.key}">
             <option value="" selected>---</option>
           <c:forEach items="${roleTypes}" var="role">
           <option value="${role.type}">
             <spring:message code="label.admin.userlist.role_${role.type}" /></option>
           </c:forEach>
           </select>
         </div>
        </th>
       </c:if>

      </c:forEach>
      <th class="search_element"><input type="submit" id="bth-search" class="btn btn-sm btn-primary" value='<spring:message code="label.table.search"/>' /></th>
     </tr>
    </tfoot>
   </table>
  </div>

 </c:if>

</div>

<script>
var table;
var actions = $("#actionList");

jQuery(document).ready(function($) {
    table = $('#example').DataTable({
         "searching": false,
         "bSortCellsTop": true,
         "responsive": true,
         "bSort" : true,
         "bDestroy": true,
         "order": [[ 3, "asc" ]],
         "oLanguage" : {
             "sEmptyTable" : jQuery.i18n.prop('dataTable.sEmptyTable'),
             "sInfo" : jQuery.i18n.prop('dataTable.sInfo'),
             "sInfoEmpty" : jQuery.i18n.prop('dataTable.sInfoEmpty'),
             "sInfoFiltered" : jQuery.i18n.prop('dataTable.sInfoFiltered'),
             "oPaginate" : {
                 "sFirst" : jQuery.i18n.prop('dataTable.sFirst'),
                 "sLast" : jQuery.i18n.prop('dataTable.sLast'),
                 "sNext" : jQuery.i18n.prop('dataTable.sNext'),
                 "sPrevious" : jQuery.i18n.prop('dataTable.sPrevious')
             },
             "sLengthMenu" : jQuery.i18n.prop('dataTable.sLengthMenu'),
             "sLoadingRecords" : jQuery.i18n.prop('dataTable.sLoadingRecords'),
             "sProcessing" : jQuery.i18n.prop('dataTable.sProcessing'),
             "sSearch" : jQuery.i18n.prop('dataTable.sSearch'),
             "sZeroRecords" : jQuery.i18n.prop('dataTable.sZeroRecords')
         },
         "serverSide": true,
         "aoColumns" : [        
              <c:forEach items="${tableSetting.columns}" var="entry" varStatus="status">
                  <c:if test="${entry.value.type eq 'search'}">
                      {
                       "sTitle" : "<spring:message code="${entry.value.title}" />",
                       "sClass": "${entry.value.data}",
                       "mData" : '${entry.key}'
                      },
                  </c:if>
                  <c:if test="${entry.value.type eq 'button'}">
                      {
                       "sTitle" : "<spring:message code="${entry.value.title}" />",
                       "mData" : "action",
                       "bSortable": false,
                       "defaultContent": '<button  class="btn btn-sm btn-primary" id="${entry.value.buttonId}"><spring:message code="label.table.profile"/></button>'               
                      },
                  </c:if>
                  <c:if test="${entry.value.type eq 'role'}">
                      {
                        "sTitle" : "<spring:message code="${entry.value.title}" />",
                        "mData" : '${entry.key}',
                        "sClass": "${entry.value.data}",
                        "mRender": function ( data, type, full ) {
                            return  jQuery.i18n.prop("msg.role."+data);
                         }
                      },
                 </c:if>
                      <c:if test="${entry.value.type eq 'status'}">
                      {
                        "sTitle" : "",
                        "mData" : '${entry.key}',
                        "bSortable": false,
                        "sClass": "${entry.value.data}",
                        "mRender": function ( data, type, full ) {
                            return  jQuery.i18n.prop("msg.status."+data);
                         },
                      },
                 </c:if>
              </c:forEach>
             ],
             "ajax": {
                "url":"${tableSetting.url}",
                "type":"POST",
                dataType: "json",
                contentType: 'application/json; charset=utf-8',
                "data": function ( data ) {
                  addSearchValue(data);
                $("#example_wrapper").prepend(actions);
                  return JSON.stringify(data);
                }
            }
     }); 
    
    $("#example_wrapper").prepend(actions);
    
    function addSearchValue(data) {
        data.tableName = '${tableSetting.tableName}';
        console.log("data.tableName = "+data.tableName);
        for (var i = 0; i < data.columns.length; i++) {
            column = data.columns[i];
            column.search.compareSign = $('#searchTypeIndex'+i).val();
            column.search.value = $('#inputIndex'+i).val();
        }
    }
  
    
});
</script>