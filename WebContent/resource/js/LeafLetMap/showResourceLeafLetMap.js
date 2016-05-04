var map;
var myLatlng;
var centerlat = $("#openMap").attr("centerlat");
var centerlng = $("#openMap").attr("centerlng");

$(document).ready(initialize());

function initialize() {
	

		var map = L.map('openMap', {
		editable : true
	}).setView([ centerlat, centerlng ], 13);

	L
			.tileLayer(
					'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw',
					{
						maxZoom : 25,
						attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
								+ '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, '
								+ 'Imagery © <a href="http://mapbox.com">Mapbox</a>',
						id : 'mapbox.streets'
					}).addTo(map);

	var bounds;

	$(".polygon").each(
			function() {
				var polygonCoords = [];
				var latArray = [];
				var lngArray = [];

				   $(this).find(".coordinatesPoint").each(function() {
					      var latitudeDegrees = Number($(this).find(".latitudeDegrees").html());
					      var latitudeMinutes = Number($(this).find(".latitudeMinutes").html());
					      var latitudeSeconds = Number($(this).find(".latitudeSeconds").html().replace(",","."));
					      var longitudeDegrees = Number($(this).find(".longitudeDegrees").html());
					      var longitudeMinutes = Number($(this).find(".longitudeMinutes").html());
					      var longitudeSeconds = Number($(this).find(".longitudeSeconds").html().replace(",","."));

					      var lat = latitudeDegrees + latitudeMinutes / 60 + latitudeSeconds / 3600;
					      var lng = longitudeDegrees + longitudeMinutes / 60 + longitudeSeconds / 3600;

					      var latLng = [ lat, lng ];

							latArray.push(lat);
							lngArray.push(lng);

							polygonCoords.push(latLng);
					    });

				maxLat = Math.max.apply(Math, latArray);
				maxLng = Math.max.apply(Math, lngArray);

				minLat = Math.min.apply(Math, latArray);
				minLng = Math.min.apply(Math, lngArray);

				bounds = [ [ minLat, minLng ], [ maxLat, maxLng ] ];

				var drawedPolygon = L.polygon(polygonCoords, {
					color : 'red',
					clickable : 'true',
					weight : '3'
				}).addTo(map);

			});
	map.fitBounds(bounds);
}

$(document).on("click", "#geoCoords", function() {
  if ($(this).hasClass("active")) {
    $(this).removeClass("active");
    $(".coordinates").hide();
    $(this).find(".glyphicon-triangle-right").show();
    $(this).find(".glyphicon-triangle-bottom").hide();
  } else {
    $(this).addClass("active");
    $(".coordinates").show();
    $(this).find(".glyphicon-triangle-right").hide();
    $(this).find(".glyphicon-triangle-bottom").show();
  }
});

