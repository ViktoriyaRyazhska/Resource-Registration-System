$(function() {
    
    var inactiveList = $(".commun[type=0]")
    $.each(inactiveList, function(index, element) {
        $(element).addClass("hide")
    })
    $("#inactiveCheckbox").click(function() {
        var inactiveList = $(".commun[type=0]")

        $.each(inactiveList, function(index, element) {
            if ($("#inactiveCheckbox").is(":checked")) {
                $(element).removeClass("hide")
            } else {
                $(element).addClass("hide")
            }
        })

    })

    
    
  $(document).on("click", "#deletecommunity", function(event) {
    event.preventDefault();
    if($("#deletecommunity").attr("disabled") != undefined)return;
    bootbox.confirm(jQuery.i18n.prop('msg.confirmDeleteCommunity'), function(result) {
      if (result) {
        $.ajax({
          url : $(event.target).attr("href"),
          type : "DELETE",

          success : function(data) {
            var tr = $(event.target).closest("tr");
            
            if(data == true){
            	tr.css("background-color", "#000000");
                tr.fadeIn(1000).fadeOut(200, function() {
                  tr.remove();
                })
            }else{
            	$("#deletecommunity",tr).attr("disabled","")
            }
          },
          error : function() {
            bootbox.alert(jQuery.i18n.prop('msg.canNotDeleteCommunity'));
          }

        });
      } else {
        event.preventDefault();
      }
      event.preventDefault();
    });
  });
  
  $(document).on("click", "#activecommunity", function(event) {
	    event.preventDefault();
	    if($("#activecommunity").attr("disabled") != undefined)return;
	    bootbox.confirm(jQuery.i18n.prop('msg.confirmActivateCommunity'), function(result) {
	      if (result) {
	        $.ajax({
	          url : $(event.target).attr("href"),
	          type : "POST",

	          success : function(data) {
	            var tr = $(event.target).closest("tr");
	            	$("#activecommunity",tr).attr("disabled","")
	          },
	          error : function() {
	            bootbox.alert(jQuery.i18n.prop('msg.canNotDeleteCommunity'));
	          }

	        });
	      } else {
	        event.preventDefault();
	      }
	      event.preventDefault();
	    });
	  });
  
})