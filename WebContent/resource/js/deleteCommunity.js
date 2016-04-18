$(function() {
  $(document).on("click", "#deletecommunity", function(event) {
    event.preventDefault();
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
            	$("#deletecommunity",tr).fadeOut();
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
	    bootbox.confirm(jQuery.i18n.prop('msg.confirmActivateCommunity'), function(result) {
	      if (result) {
	        $.ajax({
	          url : $(event.target).attr("href"),
	          type : "POST",

	          success : function(data) {
	            var tr = $(event.target).closest("tr");
	            	$("#activecommunity",tr).fadeOut();
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