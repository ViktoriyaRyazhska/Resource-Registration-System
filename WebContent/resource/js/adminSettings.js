$(document).ready(function() {
  //$("#confirmRegistrationMethod").click(function() {
  //
  //  $.ajax({
  //    type : "POST",
  //    url : "settings",
  //    data : $("#сhangeReg").serialize(),
  //    success : function(data) {
  //      bootbox.alert(jQuery.i18n.prop('msg.settingsChanged'));
  //    }
  //  });
  //
  //  return false;
  //});

  $("#checkSMTP").click(function() {

    $.ajax({
      type : "POST",
      url : "checkParametersSMTP",
      data : $("#сhangeReg").serialize(),
      success : function(data) {
        bootbox.alert(jQuery.i18n.prop('msg.settingsChanged'));
      }
    });

    return false;
  });
});