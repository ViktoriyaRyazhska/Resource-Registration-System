/**
 * author Pavlo Antentyk
 */

function checkInterseption(upolygons){
	if (isPolygonsSelfCross(upolygons)) return true;
	if (upolygons.length>1){
		if (isDrawnPolygonsCross(upolygons)) return true;
	}
	if (isNeighborPolygonsCross(upolygons)) return true;
	return false;
}	
	
/**
 * we have to check if at least one point of one polygon 
 * locate inside another to except situation 
 * when one polygon completely locate inside another  
 * 
 */	
function isPointInsidePolygon(point, upoly) {
    var polyPoints = upoly._latlngs;       
    var x = point.lat; 
    var y = point.lng;

    var inside = false;
    for (var i = 0, j = polyPoints.length - 1; i < polyPoints.length; j = i++) {
        var xi = polyPoints[i].lat, yi = polyPoints[i].lng;
        var xj = polyPoints[j].lat, yj = polyPoints[j].lng;

        var intersect = ((yi > y) != (yj > y))
            && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }

    return inside;
};


function isNeighborPolygonsCross(upolygons){
	var neighborPolygons = [];
	var isCross = false;
	
	for (var i = 0; i<upolygons.length; i++){
		currentPoly =  upolygons[i];
		neighborPolygons = getNeighborPolygons(currentPoly);
		for(var j = 0; j<neighborPolygons.length; j++){
			
			isInside1 = isPointInsidePolygon(currentPoly._latlngs[0],neighborPolygons[j]);
			isInside2 = isPointInsidePolygon(neighborPolygons[j]._latlngs[0],currentPoly);
			
			isCross = checkTwoPolygonsCross(currentPoly,neighborPolygons[j]);
			if (isCross || isInside1 || isInside2){
				console.log("NeighborPolygonsCross = "+true+" polyNumber="+i);
				return true;
			} 
		}
	}
	return isCross;
	
}
	



function isDrawnPolygonsCross(upolygons){
	var isCross = false;
	for(var i = 0; i<upolygons.length; i++){
		var poly1 = upolygons[i];
		if(i == upolygons.length - 1){
			var poly2 = upolygons[0];
		}else{
			var poly2 = upolygons[i+1];
		}
		
		isInside1 = isPointInsidePolygon(poly1._latlngs[0],poly2);
		isInside2 = isPointInsidePolygon(poly2._latlngs[0],poly1);
		
		isCross = checkTwoPolygonsCross(poly1,poly2);
		if (isCross || isInside1 || isInside2){
			console.log("DrawnPolygonsCross = "+true+" polyNumber="+i);
			return true;
		} 
	}
	return isCross;
}

function checkTwoPolygonsCross(poly1,poly2){
	var lines1 = extractLinesfromPol(poly1);
	var lines2 = extractLinesfromPol(poly2);
	var isCross = false;
	
	for(var i = 0; i<lines1.length; i++){
		line1 = lines1[i];
		for(var j = 0; j<lines2.length; j++){
			line2 = lines2[j];
			isCross = checkCrossTwoLines(line1, line2, true);
			if (isCross){
				return isCross;
			} 
		}
	}
	return isCross;
}



/**
 * check each polygon for lines self cross
 * @param upolygons
 * @returns
 */
function isPolygonsSelfCross(upolygons){
	var isCross = false;
	for(var i = 0; i<upolygons.length; i++){
		var currentPoly = upolygons[i];
		var lines = extractLinesfromPol(currentPoly);
		//check if current polygon have cross own lines;
		isCross = checkSinglePolygonLinesCross(lines);
		if (isCross){
			console.log("SelfCross = "+isCross+" polyNumber="+i);
			return isCross;
		}
	}
	return isCross;
}

/**
 * input lines of one polygon
 * check if they cross
 * @param lines 
 * @returns
 */
function checkSinglePolygonLinesCross(lines){
	var isCross = false;
	for(var i = 0; i<lines.length; i++){
		line1 = lines[i];
		for(var j = i+1; j<lines.length; j++){
			line2 = lines[j];
			isCross = checkCrossTwoLines(line1, line2, false);
			if (isCross){
				return isCross;
			} 
		}
	}
	return isCross;
}


function getNeighborPolygons(upolygon){
	var neighborPolygons = [];
	var resType1 = $("#resourcesTypeSelect").val();
	var identifier = $("#identifier").val();
	
	if (upolygon.getBounds() === undefined)
	    return;
	  var bounds = upolygon.getBounds();
	  var maxLat = bounds._northEast.lat;
	  var minLat = bounds._southWest.lat;
	  var maxLng = bounds._northEast.lng;
	  var minLng = bounds._southWest.lng;
	  
	  /*var multiplier = 0.01;
	  maxLat = maxLat * multiplier;
	  minLat = minLat * multiplier;
	  maxLng = maxLng * multiplier;
	  minLng = minLng * multiplier;*/

	  $("#dark_bg").show();
	  $.ajax({
	    data : {
	      "minLat" : minLat,
	      "maxLat" : maxLat,
	      "minLng" : minLng,
	      "maxLng" : maxLng,
	      "page" : 0,
	      "resType" : resType1
	    },
	    type : "POST",
	    async : false,
	    url : baseUrl.toString() + "/registrator/resource/getResourcesByAreaLimits",
	    timeout : 20000,
	    contentType : "application/x-www-form-urlencoded;charset=UTF-8",
	    dataType : 'json',
	    success : function(data) {
			
			if (data.polygons && data.polygons.length>0) {
				for (i = 0; i < data.polygons.length; i++) {
					
					var polygon = data.polygons[i];
							
					var points = polygon.points;
					var polygonPath = [];
					
					for (var j = 0; j < points.length; j++) {
				        var myLatLng = [points[j].latitude, points[j].longitude];
				        polygonPath.push(myLatLng);
				       
				      }
					if(polygon.identifier!=identifier){
						var neighborPolygon = L.polygon(polygonPath);
						neighborPolygons.push(neighborPolygon);
					}
					
				}
			}
			
	      $("#dark_bg").hide();
	      
	      
	    },
	    error : function() {
	      $("#dark_bg").hide();
	      bootbox.alert(jQuery.i18n.prop('msg.error'));
	    }
	  });
	  
	  return neighborPolygons;
}


/**
 * return true if cross
 * if touch true then return true even if lines touch
 * false needed when checks lines of one polygon
 * 
 */
function checkCrossTwoLines(line1, line2,touch){
	ax1 = line1[0].lat;
	ay1 = line1[0].lng;
	ax2 = line1[1].lat;
	ay2 = line1[1].lng;
	
	bx1 = line2[0].lat;
	by1 = line2[0].lng;
	bx2 = line2[1].lat;
	by2 = line2[1].lng;
	
	var v1=(bx2-bx1)*(ay1-by1)-(by2-by1)*(ax1-bx1);
	var v2=(bx2-bx1)*(ay2-by1)-(by2-by1)*(ax2-bx1);
	var v3=(ax2-ax1)*(by1-ay1)-(ay2-ay1)*(bx1-ax1);
	var v4=(ax2-ax1)*(by2-ay1)-(ay2-ay1)*(bx2-ax1);
	var v12 = v1*v2;
	var v34 = v3*v4;
	if(touch){
		var bool12 = (v12<=0);
		var bool34 = (v34<=0);
	}else{
		var bool12 = (v12<0);
		var bool34 = (v34<0);
	}
	return bool12 && bool34;
}


function extractLinesfromPol(upoly){
	var lines = [];
	var line = [];
	var polyPoints = upoly._latlngs; 

	for (var i=0; i<polyPoints.length; i++){
		if (i == polyPoints.length-1){
			line = [polyPoints[i],polyPoints[0]];
		}else{
			line = [polyPoints[i],polyPoints[i+1]];
		}
		
		lines.push(line);	
	}	
	
	return lines;
}




















