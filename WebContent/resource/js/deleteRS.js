$(function() {
	$(document).on("click","#deleterestype",
					function(event) {
		event.preventDefault();
						bootbox.confirm("Ви впевнені, що хочете видалити цей підклас?",function(result){
						if (result) {
							$
									.ajax({
										url : $(event.target).attr("href"),
										type : "DELETE",

										success : function() {
											var tr = $(event.target).closest(
													"tr");
											tr.css("background-color",
													"#000000");
											tr.fadeIn(1000).fadeOut(200,
													function() {
														tr.remove();
													})
										},
										error : function() {
											  bootbox.alert("Даний підклас видалити неможливо, " +
											  		"оскільки уже зареєстровані ресурси даного підкласу");
										}

									});
						} else {
							event.preventDefault();
						}
						event.preventDefault();
					});
});})