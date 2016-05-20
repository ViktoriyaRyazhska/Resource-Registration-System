var map;
var markers = [];
var rectangles = [];
var polygons = [];
var boundsArray = [];
var resTypes = [];
var oTable = null;



function getCookie(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ')
      c = c.substring(1);
    if (c.indexOf(name) == 0)
      return c.substring(name.length, c.length);
  }
  return "";
}

function searchOnMapByPoint(latLng, page) {

  map.setView(latLng,13);
  var lat = latLng.lat;
  var lng = latLng.lng;
  var page = page || 0;

  coordinatesCookie(lat, lng);

  $("#dark_bg").show();
  $.ajax({
    data : {
      "lat" : lat,
      "lng" : lng,
      "page" : page
    },
    type : "POST",
    url : baseUrl.toString() + "/registrator/resource/getResourcesByPoint",
    timeout : 20000,
    contentType : "application/x-www-form-urlencoded;charset=UTF-8",
    dataType : 'json',
    success : function(data) {
    	polygons = data.polygons;
    	drawPolygons(data.polygons);
        createDataTable(data.polygons);
        createFilter();

      $("#dark_bg").hide();
    },
    error : function() {
      $("#dark_bg").hide();
      bootbox.alert(jQuery.i18n.prop('msg.error'));
    }
  });
}

function searchOnMapByArea(rectangle, page) {
  var maxLat = rectangle._northEast.lat;
  var minLat = rectangle._southWest.lat;
  var maxLng = rectangle._northEast.lng;
  var minLng = rectangle._southWest.lng;
  var page = page || 0;

 
  $("#dark_bg").show();
  $.ajax({
    data : {
      "minLat" : minLat,
      "maxLat" : maxLat,
      "minLng" : minLng,
      "maxLng" : maxLng,
      "resType" : "all",
      "page" : page
    },
    type : "POST",
    url : baseUrl.toString() + "/registrator/resource/getResourcesByAreaLimits",
    timeout : 60000,
    contentType : "application/x-www-form-urlencoded;charset=UTF-8",
    dataType : 'json',
    success : function(data) {
      polygons = data.polygons;	
      drawPolygons(data.polygons);
      createDataTable(data.polygons);
      createFilter();

      $("#dark_bg").hide();
    },
    error : function() {
      $("#dark_bg").hide();
      bootbox.alert(jQuery.i18n.prop('msg.error'));
    }
  });


}

function searchByParameters(page) {

  var json = new Object();
  json.discreteParameters = [];
  json.linearParameters = [];

  json.resourceTypeId = $("#resourcesTypeSelect").val();
  json.page = page || 0;

  $("#dark_bg").show();

  $(".discreteParameter").each(function() {
    var id_param = $(this).attr("param_id");
    var compare_sign = $(this).find(".compare").val();
    var value = $(this).find(".value").val();
    if (value) {
      var node = new Object();
      node.id = id_param;
      node.compare = compare_sign;
      node.value = value;
      json.discreteParameters.push(node);

    }
  });

  var compare_sign = "linear";
  $(".linearParameter").each(function() {
    var id_param = $(this).attr("param_id");
    var value = $(this).find(".value").val();
    if (value) {

      var node = new Object();
      node.id = id_param;
      node.compare = compare_sign;
      node.value = value;
      json.linearParameters.push(node);

    }
  });

  $.ajax({
    type : "POST",
    url : baseUrl.toString() + "/registrator/resource/resourceSearch",
    data : JSON.stringify(json),
    contentType : 'application/json; charset=utf-8',
    timeout : 60000,
    dataType : 'json',
    success : function(data) {
      polygons = data.polygons;
      createDataTable(data.polygons);
      drawPolygons(data.polygons);
      
      
      
      $("#dark_bg").hide();
    },
    error : function() {
      $("#dark_bg").hide();
      bootbox.alert(jQuery.i18n.prop('msg.error'));
    }
  });
}

function createDataTable(json) {
	
  if (json.length > 0 > 0) {
    var url = baseUrl.toString() + "/registrator/resource/get?id=";

    var description = jQuery.i18n.prop('msg.description');
    var type = jQuery.i18n.prop('msg.subclass');
    var identifier = jQuery.i18n.prop('msg.identifier');
    var date = jQuery.i18n.prop('msg.date');
    var details = jQuery.i18n.prop('msg.more');

    $("#searchResult").html('<table id="datatable" class="table table-striped table-bordered" cellspacing="0"></table>');
    oTable = $('#datatable').DataTable({
      "responsive" : true,
      "aaData" : json,
      "bAutoWidth": false,
      "aoColumns" : [ {
        "sTitle" : description,
        "mData" : "resourceDescription"
      }, {
        "sTitle" : type,
        "mData" : "resourceType"
      }, {
        "sTitle" : identifier,
        "mData" : "identifier"
      }, {
        "sTitle" : date,
        "mData" : "date"
      }, {
        "sTitle" : details,
        "mData" : null
      } ],
      "aoColumnDefs" : [ // Adding URL to the last column
      {
        "aTargets" : [ 4 ], // Column to target
        "mRender" : function(json) {
          return '<a href="' + url + json.identifier + '">' + details + '</a>';
        }
      } ]
    });
  } else {
    $('#searchResult').html(jQuery.i18n.prop('msg.resourcesNotFound'));
  }
}



function createPagination(count, page) {
  var pagination = $('<ul class="pagination"></ul>');
  var paginationDiv = $('#paginationDiv');
  var page = page || 0;
  paginationDiv.html("");
  if (count > 200) {
    var pages = Math.floor(count / 200) + 1;
    for (var i = 0; i < pages; i++) {
      var begin = i * 200 + 1;
      var end = (i + 1) * 200;
      if (end > count) {
        end = count;
      }
      var paginationLi = $('<li><a class="pageA" href="#" page="' + i + '">' + begin + ' - ' + end + '</a></li>');
      if (i == page) {
        paginationLi.addClass('active');
      }
      pagination.append(paginationLi);
    }
    paginationDiv.append(pagination);
  }
}

function coordinatesCookie(lat, lng) {
  // Expires date for cookie
  var d = new Date();
  d.setTime(d.getTime() + 7 * 24 * 60 * 60 * 1000);
  // Deleting of the old cookie
  document.cookie = "LastMapSearchLat=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
  document.cookie = "LastMapSearchLng=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
  // Add new cookie
  document.cookie = "LastMapSearchLat=" + lat + ";expires=" + d.toUTCString();
  document.cookie = "LastMapSearchLng=" + lng + ";expires=" + d.toUTCString();

}

function createFilter(){
	resTypes = [];
	$("#resTypeFilter").html('');
	polygons.forEach(function(upoly){
  	  if ($.inArray(upoly.resourceType, resTypes) == (-1)) {
            resTypes.push(upoly.resourceType);
  	  }
    });
    
    if (resTypes.length > 0) {
      var resTypeFilter = "<p>Фільтр:</p>";
      for (var i = 0; i < resTypes.length; i++) {
        resTypeFilter += '<button class="btn btn-default btn-filter">' + resTypes[i] + '</button>';
      }
    }
    $("#resTypeFilter").html(resTypeFilter);
}

$(document).ready(function() {
  $.post(baseUrl.toString() + "/registrator/resource/getResourcesByTypeId", {
    "resourceTypeId" : $("#resourcesTypeSelect").val()
  }, function(data) {
    $("#searchParameters").html(data);
    $("#table").html("");
  });
});


$(document).on("click", "#searchOnMapButton", function() {
  var latitudeDegrees = Number($(".latitudeDegrees").val());
  var latitudeMinutes = Number($(".latitudeMinutes").val());
  var latitudeSeconds = Number($(".latitudeSeconds").val());

  var longitudeDegrees = Number($(".longitudeDegrees").val());
  var longitudeMinutes = Number($(".longitudeMinutes").val());
  var longitudeSeconds = Number($(".longitudeSeconds").val());

  var searchLat = latitudeDegrees + latitudeMinutes / 60 + latitudeSeconds / 3600;
  var searchLng = longitudeDegrees + longitudeMinutes / 60 + longitudeSeconds / 3600;
  //var latLng = [searchLat, searchLng];
  var latLng = new Object();
  latLng.lat = searchLat;
  latLng.lng = searchLng;
  addNewMarker(latLng);

  searchOnMapByPoint(latLng);
});

$(document).on('click', '#searchByPointButton, #searchByAreaButton, #searchByParameterButton', function(){
	clearMarker();
	$("#searchResult").html('');//clear dataTable
})



$(document).on("click", "#searchOnMapButton_area", function() {
  var firstPoint = $("#first_point");
  var secondPoint = $("#second_point");

  var firstLatDeg = Number(firstPoint.find(".latitudeDegrees").val());
  var firstLatMin = Number(firstPoint.find(".latitudeMinutes").val());
  var firstLatSec = Number(firstPoint.find(".latitudeSeconds").val());
  var firstLngDeg = Number(firstPoint.find(".longitudeDegrees").val());
  var firstLngMin = Number(firstPoint.find(".longitudeMinutes").val());
  var firstLngSec = Number(firstPoint.find(".longitudeSeconds").val());

  var secondLatDeg = Number(secondPoint.find(".latitudeDegrees").val());
  var secondLatMin = Number(secondPoint.find(".latitudeMinutes").val());
  var secondLatSec = Number(secondPoint.find(".latitudeSeconds").val());
  var secondLngDeg = Number(secondPoint.find(".longitudeDegrees").val());
  var secondLngMin = Number(secondPoint.find(".longitudeMinutes").val());
  var secondLngSec = Number(secondPoint.find(".longitudeSeconds").val());

  var firstLat = firstLatDeg + firstLatMin / 60 + firstLatSec / 3600;
  var firstLng = firstLngDeg + firstLngMin / 60 + firstLngSec / 3600;

  var secondLat = secondLatDeg + secondLatMin / 60 + secondLatSec / 3600;
  var secondLng = secondLngDeg + secondLngMin / 60 + secondLngSec / 3600;

  var north;
  var south;
  var east;
  var west;

  if (firstLat > secondLat) {
    north = firstLat;
    south = secondLat;
  } else {
    north = secondLat;
    south = firstLat;
  }

  if (firstLng > secondLng) {
    east = firstLng;
    west = secondLng;
  } else {
    east = secondLng;
    west = firstLng;
  }

  // Clear out the old rectangles.
  /*rectangles.forEach(function(rectangle) {
    rectangle.setMap(null);
  });*/
  rectangles = [];

  var rectangle = new Object({
     bounds : {
      north : north,
      south : south,
      east : east,
      west : west
    }
  });

  rectangles.push(rectangle);

  searchOnMapByArea(rectangle);
});

$(document).on("click", ".toggle-button", function() {
  if (!$(this).hasClass("active")) {
    $(this).siblings().removeClass("active");
    $(this).addClass("active");
    var id = $(this).attr("id");
    id = id.substr(0, id.length - 6);
    $(".searchDiv").hide();
    $("#" + id + "Div").show();
  }
});



$(document).on("click", ".btn-filter", function() {
  if (!$(this).hasClass("active")) {
    $("#dark_bg").show();
    $(this).siblings().removeClass("active");
    $(this).addClass("active");
    var resType = $(this).html();
    for (var i = 0; i < polygons.length; i++) {
      if (polygons[i].resType == resType)  {
        
    	  polygons[i].setMap(map);
      } else if ((polygons[i].map != null) && (polygons[i].resType != resType)) {
        polygons[i].setMap(null);
      }
    }
    $("#dark_bg").hide();
  } else {
    $("#dark_bg").show();
    $(this).removeClass("active");
    for (var i = 0; i < polygons.length; i++) {
      if (polygons[i].map == null) {
        polygons[i].setMap(map);
      }
    }
    $("#dark_bg").hide();
  }
});

$(document).on("change", "#resourcesTypeSelect", function() {
  $("#dark_bg").show();
  $.post(baseUrl.toString() + "/registrator/resource/getResourcesByTypeId", {
    "resourceTypeId" : $("#resourcesTypeSelect").val()
  }, function(data) {
    $("#searchParameters").html(data);
    $("#table").html("");
    $("#dark_bg").hide();
  });
});

$(document).on("click", "#search", function() {
  searchByParameters();
});



$(document).on("click", "#showAllResources", function() {
  $("#dark_bg").show();

  var resType = $("#resourcesTypeSelect").val();

  $.ajax({
    type : "POST",
    url : baseUrl.toString() + "/registrator/resource/showAllResources",
    data : {
      "resType" : resType
    },
    contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
    timeout : 60000,
    dataType : 'json',
    success : function(data) {
    	polygons = data.polygons;
    	drawPolygons(data);
        createDataTable(data);
        
        $("#dark_bg").hide();
    },
    error : function() {
      $("#dark_bg").hide();
      bootbox.alert(jQuery.i18n.prop('msg.error'));
    }
  });

});

/*$(document).on("click", "#datatable tbody tr", function() {
  if (!$(this).hasClass("clickedTr")) {
    if ($(".clickedTr").length > 0) {
      var prevPolygonId = $(".clickedTr").attr("id").substr(3);
      $(".clickedTr").removeClass("clickedTr");
      if ((rectangles.length == 0) && (markers.length == 0)) {
        polygons[prevPolygonId].setMap(null);
      }
    }

    var polygonId = $(this).attr("id").substr(3);
    $(this).addClass("clickedTr");
    polygons[polygonId].setMap(map);
    map.fitBounds(boundsArray[polygonId]);
  }
});*/

$(document).on("click", "#paginationDiv .pageA", function() {
  var page = $(this).attr("page");
  // alert ("Hi!");
  // console.log("rectangle: " + rectangles.length);
  if (markers.length > 0) {
    searchOnMapByPoint(markers[0], page);
  } else if (rectangles.length > 0) {
    searchOnMapByArea(rectangles[0], page);
  } else {
    searchByParameters(page);
  }

});


