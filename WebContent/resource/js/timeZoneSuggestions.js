jQuery(document).ready(function ($) {

    $("#time_id").keyup(function () {
        if (!this.value) {
            $("#time_id").val('');
        }
    });

    $('#time_id').autocomplete({
        serviceUrl: baseUrl.toString() + '/timeZones',
        paramName: "value",
        delimiter: ",",
        minChars: 2,
        autoSelectFirst: true,
        deferRequestBy: 300,
        type: "GET",
        onSearchStart: function () {
            // clearData();
        },
        transformResult: function (response) {
            return {
                suggestions: $.map($.parseJSON(response), function (item) {
                    return {
                        value: item.description,
                        data: item.id
                    };
                })
            };
        },
        onSelect: function (suggestion) {
            $("#time_id").val(suggestion.data);
        }

    });

});
