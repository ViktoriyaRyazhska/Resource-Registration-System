var BH = function () {
    var window_height = $(window).height();
    var body_height = $("#body").height();
    var header_height = $("#header").height();
    var menu_height = $("#menu").height();
    var footer_height = $("#footer").height();
    var new_height = window_height - header_height - menu_height - footer_height - 100;
    console.log("window_height: " + window_height + " body_height: " + body_height);
    if (body_height < new_height) {
        $("#body").height(new_height);
    }
}

$(document).ready(function () {
    var getUrl = window.location;
    var baseUrl = getUrl .protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];

    $(window).load(function () {
        //BH();

    //    Count resources in footer
        $.post(baseUrl.toString() + "/registrator/resource/countResources", {"count": "true"}, onPostSuccess);
        function onPostSuccess (data) {
            if(data.toString().length < 5) {
                data = ""+data;
            }
            $("#count").html(data);
        }
    });

    $("#datatable").DataTable({

        }
    );

    $("#body").children("div").resize(BH());
});