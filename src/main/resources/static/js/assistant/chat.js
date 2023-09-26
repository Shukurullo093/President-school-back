let baseUrl = window.location.origin;

// setInterval(function (){
//     $('#test').text(new Date());
//     refresh();
// }, 1000);

function refresh(){
    $.ajax({
        url: baseUrl + "/api/assistant/rest/refresh",
        type: 'GET',
        enctype: 'application/json',
        data: null,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            $('#ul').html(data);
        },
        error: function (e) {
            console.log(e);
        }
    })
}

function deleteMsg(th){
    $.ajax({
        url: baseUrl + "/api/assistant/rest/delete/msg/" + $(th).attr('data-id'),
        type: 'DELETE',
        enctype: 'application/json',
        data: null,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.statusCode === 200){
                refresh();
            }
        },
        error: function (e) {
            console.log(e);
        }
    })
}

$(".la-send").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);

    $.ajax({
        url: baseUrl + "/api/assistant/rest/send/" + $('.la-send').attr('data-id'),
        type: 'POST',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            form.reset();
            refresh();
        },
        error: function (e) {
            console.log(e);
        }
    })
});

// ngrok http --domain=president-school.ngrok-free.app 8080