/**
 * Clear registration form
 */
$(".reset").click(function(event) { 
  /* for error page, we need receive from 
   * server new one (clean and shiny)
   */
  if($('#registrationForm').find(".error").length>1){
    event.preventDefault();
    window.location = window.location.href;
  }
  
});