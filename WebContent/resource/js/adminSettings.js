$(document).ready(function() {
  clearPassword = $('#smtpPasswordChanged').val() === 'false';

  $(document).ready(function(){
    $(".numeric").numeric({decimal:" ", negative:false});
  });

  $("#checkSMTP").click(function() {
    $("#dark_bg").show();
    $.ajax({
      type : "POST",
      url : "checkParametersSMTP",
      data : $("#сhangeReg").serialize(),
      success : function(data) {
        $("#dark_bg").hide();
        if (data.status === "OK") {
          bootbox.alert(jQuery.i18n.prop('msg.settings.okSMTP'));
        } else {
          bootbox.alert(jQuery.i18n.prop('msg.settings.errorSMTP'));
        }

      },
      error : function(jqXHR, textStatus, errorThrown) {
        $("#dark_bg").hide();
      }
    });

    return false;
  });

  $('#smtpPassword').focus(function() {
    if (clearPassword) {
      $(this).val("");
      $('#smtpPasswordChanged').val("true");
    }
  });
});