var search_fields;

$( window ).resize(function() {
    $("#toggle_dt").css('display', 'none');//hide toggle 
    $(".search_element").each(function(index){
      if($(this).css('display') == 'none'){
        console.log( index + ": none");
        search_fields = $("#toggle_dt");
        $("#toggle_dt").css('display', 'visible');//show toggle
      }
      
    });
});

$(document).ready(function(){
  $("#toggle_dt").click(function(){
      $("tfoot > tr").after('<tr class="child"> <td class="child"> '+search_fields.html()+' </td> </tr> ');
  });
});