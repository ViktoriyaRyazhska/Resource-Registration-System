/**
 * author Pavlo Antentyk
 */

$(function(){
startPoint = [49.83523, 24.03381];
var markers = new L.FeatureGroup();
var marker = new  L.marker();
var markerControl;

// create map
createMap();


// control for adding marker
L.NewMarkerControl = L.Control.extend({
	
    options: {
        position: 'topleft'
    },

    onAdd: function (map) {
        var container = L.DomUtil.create('div', 'leaflet-control leaflet-bar'),
        link = L.DomUtil.create('a', '', container);
        link.href = '#';
        link.title = 'Select point';
        link.innerHTML = "<img src=\""+baseUrl+"resource/img/leaflet/marker.png\">";
        //link.innerHTML = '⚫';
        container.style.backgroundColor = 'white';
	    container.style.width = '24px';
	    container.style.height = '24px';
        L.DomEvent.on(link, 'click', L.DomEvent.stop)
                  .on(link, 'click', function () {
                	  if ($('#searchByPointDiv').css('display')=="block"){
                		  marker = map.editTools.startMarker();
                	  }
                  });

        return container;
    }
});

// adding control to map
markerControl = new L.NewMarkerControl();
map.addControl(markerControl);


//click on map for setting searching point and adding marker to this point
map.on('editable:drawing:end',function(e){

	e.layer.dragging.disable();
	map.removeLayer(markers);
	markers = new L.FeatureGroup();
	markers.addLayer(e.layer);
	map.addLayer(markers);
    if ($('#searchByPointDiv').css('display')=="block"){
		  searchOnMapByPoint(e.layer._latlng);
	 }
	 
});

// control for selecting area
L.NewAreaSelectControl = L.Control.extend({
	
    options: {
        position: 'topleft'
    },

    onAdd: function (map) {
        var container = L.DomUtil.create('div', 'leaflet-control leaflet-bar'),
        link = L.DomUtil.create('a', '', container);
        link.href = '#';
        link.title = 'Select area';
        link.innerHTML = "<img src=\""+baseUrl+"resource/img/leaflet/areaSelect.png\">";
        //link.innerHTML = '#';
        container.style.backgroundColor = 'white';
	    container.style.width = '24px';
	    container.style.height = '24px';
        L.DomEvent.on(link, 'click', L.DomEvent.stop)
                  .on(link, 'click', function () {
                	  if ($('#searchByAreaDiv').css('display')=="block"){
                		  map.selectArea.enable();
                		  map.selectArea.setControlKey(false);
                	  }
                  });

        return container;
    }
});
//adding control to map
areaSelectControl = new L.NewAreaSelectControl();
map.addControl(areaSelectControl);

// fired when finish area select
map.on('areaselected', function(e){
	  //console.log(e); // lon, lat, lon, lat 
	  map.selectArea.disable();
	  searchOnMapByArea(e.bounds);
});

});

function clearMarker(){
	map.removeLayer(markers);
	clearMap(map);
}


