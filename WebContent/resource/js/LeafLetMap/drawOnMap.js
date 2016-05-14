/**
 * author Pavlo Antentyk
 */
// create map
function createMap(){
	map = L.map('openMap',  {editable: true}).setView(startPoint, 12);
	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
		maxZoom: 25,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(map);
}

function drawPolygons(polygons) {
	clearMap(map);
	var i;
	var infoWindowContent = "<table id='infowindow_table'><tr><th>" + jQuery.i18n.prop('msg.description') + "</th><th>" + jQuery.i18n.prop('msg.subclass') + "</th><th></th></tr>";
	var contentString = "";
	if (polygons && polygons.length>0) {
		for (i = 0; i < polygons.length; i++) {
			
			var polygon = polygons[i];
			/*Object polygon
			DT_RowId:"row0"
			date:"02.01.2016"
			identifier:"79000-001"
			points:Array[48]
			resourceDescription:"Стрийський парк"
			resourceType:"земельний"*/
			
			var points = polygon.points;
			var polygonPath = [];
			
			for (var j = 0; j < points.length; j++) {
		        var myLatLng = [points[j].latitude, points[j].longitude];
		        polygonPath.push(myLatLng);
		       
		      }
			
			var drawedPolygon = L.polygon(polygonPath, {color:'red', clickable:'true', weight:'3'}).addTo(map);
			
			drawedPolygon.on('click',function(e){
				console.log(e.target);
			});
			
			contentString = "<tr>" + "<td>" + polygon.resourceDescription + "</td>" + "<td>" + polygon.resourceType
			+ "</td>" + "<td><a href='" + baseUrl.toString() + "/registrator/resource/get/?id="
			+ polygon.identifier + "'><i>Детальніше</i></a> </td>" + "</tr>";
			drawedPolygon.bindPopup(infoWindowContent+contentString);
			
		}
	}
}

function clearMap(m) {
    for(i in m._layers) {
        if(m._layers[i]._path != undefined && m._layers[i].exeptClear!= true) {
            try {
                m.removeLayer(m._layers[i]);
            }
            catch(e) {
                console.log("problem with " + e + m._layers[i]);
            }
        }
    }
}
