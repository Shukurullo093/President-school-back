let baseUrl = window.location.origin;

function accessFunc(th){

}

$(".la-send").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);

    $.ajax({
        url: baseUrl + "/api/assistant/rest/send/1/3077943c-25d1-49ed-9c19-8c7ebb21de57/1",
        type: 'POST',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            // alert(data);
        },
        error: function (e) {
            console.log(e);
        }
    })
});