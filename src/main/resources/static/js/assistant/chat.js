let baseUrl = window.location.origin;

setInterval(function (){
    $('#test').text(new Date());
    refresh($('#taskOrderNumber').attr('data-id'));
}, 1000);

function refresh(taskOrder){
    $.ajax({
        url: baseUrl + "/api/assistant/rest/refresh/" + $('#studentId').attr('data-id') + "/" +
            $('#lessonId').attr('data-id') + "/" + taskOrder,
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

function changeChat(taskOrder){
    refresh(taskOrder);
    $('.la-send').attr('data-id', taskOrder);
    $('#taskOrderNumber').text(taskOrder + '-topshiriq');
    $('#taskOrderNumber').attr('data-id', taskOrder);
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
                refresh($('.la-send').attr('data-id'));
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
        url: baseUrl + "/api/assistant/rest/send/" + $('#studentId').attr('data-id') + "/" +
            $('#lessonId').attr('data-id') + "/" + $('#taskOrderNumber').attr('data-id'),
        type: 'POST',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            form.reset();
            refresh($('#taskOrderNumber').attr('data-id'));
        },
        error: function (e) {
            console.log(e);
        }
    })
});