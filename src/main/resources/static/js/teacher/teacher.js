let baseUrl = window.location.origin;

$('#lessonBtn').click(function (){
    let form = $('#form')[0];
    let dataForm = new FormData(form);
    dataForm.set('lessonCount', dataForm.get('lessonCount').slice(1));

    $.ajax({
        url: baseUrl + "/api/teacher/rest/add/lesson",
        type: 'POST',
        enctype: 'multipart/form-data',
        data: dataForm,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if(data['statusCode'] === 200){
                $("#form .alert-success").css('display', 'block');
                $("#form .alert-success .text-dark").text(data['message']);
                $('#lessonBtn').attr('disabled', true);
                //  task form
                $('textarea[name=task-description]').attr('disabled', false);
                $('input[name=task-img-source]').attr('disabled', false);
                $('input[name=task-answer]').attr('disabled', false);
                $('textarea[name=task-example-description]').attr('disabled', false);
                $('input[name=task-example-img-source]').attr('disabled', false);
                $('#taskBtn').attr('disabled', false);
                $("#taskBtn").attr('data-id', data.object);
                 // test form
                $('textarea[name=questionTxt]').attr('disabled', false);
                $('input[name=questionImg]').attr('disabled', false);
                $('textarea[name=v1]').attr('disabled', false);
                $('input[name=v1img]').attr('disabled', false);
                $('textarea[name=v2]').attr('disabled', false);
                $('input[name=v2img]').attr('disabled', false);
                $('textarea[name=v3]').attr('disabled', false);
                $('input[name=v3img]').attr('disabled', false);
                $('#testBtn').attr('disabled', false);
                $("#testBtn").attr('data-id', data.object);

                setTimeout(function(){
                    $("#form .alert-success").css('display', 'none');
                    $("#form .alert-success .text-dark").text('');

                    $('input[name=title]').attr('disabled', true);
                    $('textarea[name=description]').attr('disabled', true);
                    $('input[name=source]').attr('disabled', true);
                    // let num = parseInt(dataForm.get('lessonCount')) + 1;
                    // form.reset();
                    // $('input[name="lessonCount"]').val("#" + num);
                    // if (num <= 2){
                    //     $('input[name="lessonType"]').val("Demo");
                    //     $('#v1').css("display", "block");
                    //     $('#t1').css("display", "block");
                    // } else {
                    //     $('input[name="lessonType"]').val("Video");
                    //     $('#v1').css("display", "block");
                    //     $('#t1').css("display", "block");
                    // }
                }, 3000);
            }
            else {
                $("#form .alert-danger").css('display', 'block');
                $("#form #error-alert").text(data['message']);
                setTimeout(function(){
                    $("#form .alert-danger").css('display', 'none');
                    $("#form #error-alert").text("");
                }, 4000);
            }
        },
        error: function (e) {
            console.log(e);
        }
    })
});

$('#taskBtn').click(function (){
    let form = $('#task-form')[0];
    let dataForm = new FormData(form);
    // dataForm.set('lessonCount', dataForm.get('lessonCount').slice(1));

    $.ajax({
        url: baseUrl + "/api/teacher/rest/add/" + $('#taskBtn').attr('data-id') + "/task",
        type: 'POST',
        enctype: 'multipart/form-data',
        data: dataForm,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if(data['statusCode'] === 200){
                $("#task-form .alert-success").css('display', 'block');
                $("#task-form .alert-success .text-dark").text(data['message']);
                $('#task-table tbody').html(data.object);

                // $('#lessonBtn').attr('disabled', true);
                //  task form
                // $('textarea[name=task-description]').attr('disabled', false);
                // $('input[name=task-img-source]').attr('disabled', false);
                // $('input[name=task-answer]').attr('disabled', false);
                // $('textarea[name=task-example-description]').attr('disabled', false);
                // $('input[name=task-example-img-source]').attr('disabled', false);
                // $('#taskBtn').attr('disabled', false);
                // $("#taskBtn").attr('data-id', data.object);
                //  test form
                // $('textarea[name=questionTxt]').attr('disabled', false);
                // $('input[name=questionImg]').attr('disabled', false);
                // $('textarea[name=v1]').attr('disabled', false);
                // $('input[name=v1img]').attr('disabled', false);
                // $('textarea[name=v2]').attr('disabled', false);
                // $('input[name=v2img]').attr('disabled', false);
                // $('textarea[name=v3]').attr('disabled', false);
                // $('input[name=v3img]').attr('disabled', false);
                // $('#testBtn').attr('disabled', false);
                // $("#testBtn").attr('data-id', data.object);

                setTimeout(function(){
                    $("#task-form .alert-success").css('display', 'none');
                    $("#task-form .alert-success .text-dark").text('');
                    form.reset();
                    // $('input[name=title]').attr('disabled', true);
                    // $('textarea[name=description]').attr('disabled', true);
                    // $('input[name=source]').attr('disabled', true);
                    // let num = parseInt(dataForm.get('lessonCount')) + 1;
                    // form.reset();
                    // $('input[name="lessonCount"]').val("#" + num);
                    // if (num <= 2){
                    //     $('input[name="lessonType"]').val("Demo");
                    //     $('#v1').css("display", "block");
                    //     $('#t1').css("display", "block");
                    // } else {
                    //     $('input[name="lessonType"]').val("Video");
                    //     $('#v1').css("display", "block");
                    //     $('#t1').css("display", "block");
                    // }
                }, 3000);
            }
            else {
                $("#task-form .alert-danger").css('display', 'block');
                $("#task-form #error-alert").text(data['message']);
                setTimeout(function(){
                    $("#task-form .alert-danger").css('display', 'none');
                    $("#task-form #error-alert").text("");
                }, 4000);
            }
        },
        error: function (e) {
            console.log(e);
        }
    })
});

$('#testBtn').click(function (){
    let form = $('#test-form')[0];
    let dataForm = new FormData(form);
    if ($("#testBtn").text() === 'Saqlash'){
        $.ajax({
            url: baseUrl + "/api/teacher/rest/add/test/" + $('#testBtn').attr('data-id'),
            type: 'POST',
            enctype: 'multipart/form-data',
            data: dataForm,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if(data['statusCode'] === 200){
                    $("#test-form .alert-success").css('display', 'block');
                    $("#test-form .alert-success .text-dark").text(data['message']);
                    setTimeout(function(){
                        $("#test-form .alert-success").css('display', 'none');
                        $("#test-form .alert-success .text-dark").text('');
                        form.reset();
                        $('#test-form .btn-primary').text("Saqlash");
                    }, 3000);
                }
                else {
                    $("#test-form .alert-danger").css('display', 'block');
                    $("#test-form #error-alert").text(data['message']);
                    setTimeout(function(){
                        $("#test-form .alert-danger").css('display', 'none');
                        $("#test-form #error-alert").text("");
                    }, 4000);
                }
            },
            error: function (e) {
                console.log(e);
            }
        })
    } else {
        $.ajax({
            url: baseUrl + "/api/teacher/rest/edit/test/" + $('#testBtn').attr('data-id') + '/' + $('#testBtn').attr('data-test-id'),
            type: 'PUT',
            enctype: 'multipart/form-data',
            data: dataForm,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if(data['statusCode'] === 200){
                    $("#test-form .alert-success").css('display', 'block');
                    $("#test-form .alert-success .text-dark").text(data['message']);
                    setTimeout(function(){
                        $("#test-form .alert-success").css('display', 'none');
                        $("#test-form .alert-success .text-dark").text('');
                        form.reset();
                        $('#test-form .btn-primary').text("Saqlash");
                    }, 3000);
                }
                else {
                    $("#test-form .alert-danger").css('display', 'block');
                    $("#test-form #error-alert").text(data['message']);
                    setTimeout(function(){
                        $("#test-form .alert-danger").css('display', 'none');
                        $("#test-form #error-alert").text("");
                    }, 4000);
                }
            },
            error: function (e) {
                console.log(e);
            }
        })
    }
});

function updateLesson(id){
    let form = $('#form')[0];
    let data = new FormData(form);

    $.ajax({
        url: baseUrl + "/api/teacher/rest/edit/lesson/" + $('#editBtn').attr("data-id"),
        type: 'POST',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if(data['statusCode']===200){
                $(".alert-success").css('display', 'block');
                $(".alert-success .text-dark").text(data['message']);
                setTimeout(function(){
                    $(".alert-success").css('display', 'none');
                    $(".alert-success .text-dark").text('');
                    form.reset();
                }, 4000);
            }
            else {
                $(".alert-danger").css('display', 'block');
                $("#error-alert").text(data['message']);
                setTimeout(function(){
                    $(".alert-danger").css('display', 'none');
                    $("#error-alert").text("");
                }, 4000);
            }
        },
        error: function (e) {
            console.log(e);
        }
    })
}

function deleteTest(th){
    if (confirm("Test o'chirilsinmi?")) {
        $.ajax({
            url: baseUrl + "/api/teacher/rest/test/" + $(th).attr('data-id'),
            type: 'DELETE',
            enctype: 'application/json',
            data: null,
            processData: false,
            contentType: false,
            cache: false,
            success: function () {
                alert("O'chirildi");
            },
            error: function (e) {
                console.log(e);
            }
        })
    }
}

$("#table").on("click","tbody tr .la-edit",function (){
    let $tr = $(this).closest('tr');
    let edit=$tr.children("td").map(function (){
        return $(this).text();
    }).get();
    $('.questionTxt').val(edit[2].trim());
    $('.v1').val(edit[3].trim());
    $('.v2').val(edit[4].trim());
    $('.v3').val(edit[5].trim());
    $('.btn-primary').text("Tahrirlash");
    $('.btn-primary').attr('data-test-id', $(this).attr("data-id"));
});