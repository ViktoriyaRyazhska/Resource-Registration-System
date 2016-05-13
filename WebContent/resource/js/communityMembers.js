/*
*Author Pavlo Antentyk
*
*/
var communityName;
$(function(){
	var location = window.location.href;
	
	var usersUrl = baseUrl+"administrator/users/get-all-users";
	var communityUrl = baseUrl+'administrator/communities/show-all-communities';
	
	if(location.includes('show-all-communities')){
		$('.communName').on('click', function(e){
			communityName = e.target.innerText;
			window.location = usersUrl +'#'+communityName;
		})
	}
	
	
	
	if(location.includes('get-all-users')){
		communityName = window.location.hash.substring(1);
		if(communityName){
			$('#inputIndex4').val(communityName);
			$('#bth-search').click();
		}
	}
	
	
})
