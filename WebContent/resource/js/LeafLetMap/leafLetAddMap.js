/**
 * author Pavlo Antentyk
 */
var startPoint = [49.83523, 24.03381];
var polygonsFromMap = [];
var resType;

//create map
createMap();

// control for drawing polygons
L.NewPolygonControl = L.Control.extend({

    options: {
        position: 'topleft'
    },

    onAdd: function (map) {
        var container = L.DomUtil.create('div', 'leaflet-control leaflet-bar'),
        link = L.DomUtil.create('a', '', container);
        link.href = '#';
        link.title = 'Create a new polygon';
        //link.innerHTML = '▱';
        link.innerHTML = "<img src=\"/resources/resource/img/leaflet/polygon.png\">";
        container.style.backgroundColor = 'white';
	    container.style.width = '24px';
	    container.style.height = '24px';
        L.DomEvent.on(link, 'click', L.DomEvent.stop)
                  .on(link, 'click', function () {
                   
                	  if(resType!= undefined){               	  
                		  map.editTools.startPolygon();
                	  }else{
                		  $("#resourcesTypeSelect").focus();
                		    bootbox.alert(jQuery.i18n.prop('msg.selectType'));
                	  }
                  });

        map.editTools.on('editable:shape:new', function(){
    		console.log("editable:shape:new");
    		
    	});
        return container;
    }
});

// adding control to map
polygonControl = new L.NewPolygonControl;
map.addControl(polygonControl);

//control for Clear polygons
L.NewClearButtonControl = L.Control.extend({

	  options: {
	    position: 'topleft' 
	    //control position - allowed: 'topleft', 'topright', 'bottomleft', 'bottomright'
	  },

	  onAdd: function (map) {
		    var container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');
		    link = L.DomUtil.create('a', '', container);
		    link.title = 'Cancel';
		    link.href = '#';
		    link.innerHTML = "<img src=\"/resources/resource/img/leaflet/cancel.png\">";

		    container.style.backgroundColor = 'white';
		    container.style.width = '24px';
		    container.style.height = '24px';

		    container.onclick = function(){
		    	deletePolygons(polygons);
		    	cleanPoints();
		    }
		    return container;
		  },

});

//adding control to map
ClearButtonControl = new L.NewClearButtonControl;
map.addControl(ClearButtonControl);

//geometryUtil =  L.GeometryUtil; 

// stop drawing when polygon got three vertex
map.on('editable:drawing:click', function(e){
	if(e.layer._latlngs.length>=3){
		map.editTools.stopDrawing();
	}
})

//fired when stopDrawing and store polygon reference into array  
map.on('editable:drawing:end',function(e){

	var drawnPolygon = e.layer;
	if(e.layer._latlngs.length>=3){
		polygonsFromMap.push(drawnPolygon);
		drawnPolygon.exeptClear = true;
		if(checkInterseption(polygonsFromMap)) bootbox.alert(jQuery.i18n.prop('msg.PolygonsCross'));
		var area =  L.GeometryUtil.geodesicArea(drawnPolygon.getLatLngs());
		area = L.GeometryUtil.readableArea(area, true);
		console.log(area);
		perimetr = L.GeometryUtil.polyPerimetr(drawnPolygon);
		console.log("area="+area+"  perim="+perimetr);
		updatePointOnUI(polygonsFromMap);
	}else{
		map.removeLayer(drawnPolygon);
	}
	
	 
});

//fired when after dragging any polygon vertex
map.on('editable:vertex:dragend',function(e){
	
	//console.log('editable:vertex:dragend');
	//console.log(e);
	//dragedVertex = e.vertex;
	editedPolygon = e.layer;
	var area =  L.GeometryUtil.geodesicArea(editedPolygon.getLatLngs());
	area = L.GeometryUtil.readableArea(area, true);
	perimetr = L.GeometryUtil.polyPerimetr(editedPolygon);
	console.log("area="+area+"  perim="+perimetr);
	if(checkInterseption(polygonsFromMap)) bootbox.alert(jQuery.i18n.prop('msg.PolygonsCross'));
	updatePointOnUI(polygonsFromMap);
	
});

//used for development
map.on('contextmenu', function(e){
	console.log("right click");
	console.log(polygons);
	
});


function drawPolygonsfromUI(polygons){
	polygonsFromMap = [];
	polygons.forEach(function(polygon){
		
		var drawnPolygon = L.polygon(polygon, {color:'blue', weight:'5'}).addTo(map);
		drawnPolygon.enableEdit();
		polygonsFromMap.push(drawnPolygon);
		if(checkInterseption(polygonsFromMap)) bootbox.alert(jQuery.i18n.prop('msg.PolygonsCross'));
	})
	
	
	return polygonsFromMap;
}

function deletePolygons(polygons){
	polygons.forEach(function(polygon){
		polygon._map = map;
		map.removeLayer(polygon);
	})
	polygonsFromMap = [];
	//newPolygons = [];
}
