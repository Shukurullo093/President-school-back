let baseUrl = window.location.origin;

function createLesson(){
    let form = $('#form')[0];
    let dataForm = new FormData(form);

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
                $(".alert-success").css('display', 'block');
                $(".alert-success .text-dark").text(data['message']);
                setTimeout(function(){
                    $(".alert-success").css('display', 'none');
                    $(".alert-success .text-dark").text('');
                    let num = parseInt(dataForm.get("lessonCount").substring(1)) + 1;
                    form.reset();
                    $('input[name="lessonCount"]').val("#" + num);
                    if (num <= 2){
                        $('input[name="lessonType"]').val("Demo");
                        $('#v1').css("display", "block");
                        $('#t1').css("display", "block");
                    } else if (num % 7 === 0){
                        $('input[name="lessonType"]').val("Test");
                        $('#v1').css("display", "none");
                        $('#t1').css("display", "none");
                    } else {
                        $('input[name="lessonType"]').val("Video");
                        $('#v1').css("display", "block");
                        $('#t1').css("display", "block");
                    }
                    // if (num % 7 === 0)
                    //     window.location = "/api/teacher/add/lesson/" + dataForm.get('class');
                }, 3000);
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

function addTest(th){
    let form = $('#form')[0];
    let dataForm = new FormData(form);
    if ($(th).text() === 'Saqlash'){
        $.ajax({
            url: baseUrl + "/api/teacher/rest/add/test/" + $(th).attr('data-id'),
            type: 'POST',
            enctype: 'multipart/form-data',
            data: dataForm,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if(data['statusCode'] === 200){
                    $(".alert-success").css('display', 'block');
                    $(".alert-success .text-dark").text(data['message']);
                    setTimeout(function(){
                        $(".alert-success").css('display', 'none');
                        $(".alert-success .text-dark").text('');
                        form.reset();
                        $('.btn-primary').text("Saqlash");
                    }, 3000);
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
    } else {
        $.ajax({
            url: baseUrl + "/api/teacher/rest/edit/test/" + $(th).attr('data-id') + '/' + $(th).attr('data-test-id'),
            type: 'PUT',
            enctype: 'multipart/form-data',
            data: dataForm,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if(data['statusCode'] === 200){
                    $(".alert-success").css('display', 'block');
                    $(".alert-success .text-dark").text(data['message']);
                    setTimeout(function(){
                        $(".alert-success").css('display', 'none');
                        $(".alert-success .text-dark").text('');
                        form.reset();
                        $('.btn-primary').text("Saqlash");
                    }, 3000);
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