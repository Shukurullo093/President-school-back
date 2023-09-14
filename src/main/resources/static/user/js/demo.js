let baseUrl = window.location.origin;

$(document).ready(function () {
    $("#shop").click(function () {
        $.ajax({
            url: baseUrl + "/api/student/rest/shop/" + $('#shop').attr('data-id'),
            type: 'POST',
            enctype: 'application/json',
            data: null,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                alert(data)
            },
            error: function (e) {
                console.log(e);
            }
        })
    });
});