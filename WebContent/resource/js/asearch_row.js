/*  
 * Search row toggle button state
 */
var toggle = true;

/**
 * toggle button event click 
 */
$(document).ready(function(){
  $("#toggle_dt").click(function(){
      if( toggle ){
        copyHidedSearchFilds();
      }else{
        removeExtSearchFields();
      } 
  });

});

/**
 * copy search fields and show extended search menu.
 */
function copyHidedSearchFilds(){
  $("tfoot > tr").after('<tr class="child"> <td class="child" colspan="100%"> <div class="change_me"></div> </td> </tr> ');
  $(".search_element").each(function(index){
    if($(this).css('display') == 'none'){
      var element = $(this).clone();
      // delete NONE attr, and add css
      element.removeAttr('style');
      element.css('clear', 'both');
      element.css('float', 'left');
      element.toggleClass("search_element");
      // change id for each input field, x - extended menu
      element.find('input, select').each(function(index,elem){
         var id = elem.getAttribute('id'); 
         elem.setAttribute('id',id+"x");
      });
      // apply
      $(".change_me").append(element);
    }
  });
  toggle = false;
}

/**
 * remove extended search menu
 */
function removeExtSearchFields(){
  $("tfoot tr.child").remove();
  toggle = true;
}

/**
 * if table resizing change available 
 * copied fields
 */
$( document ).on( "table-resize-event", function(){
  if(!toggle){
    $("tfoot tr.child").remove();
    copyHidedSearchFilds();
  }
});

