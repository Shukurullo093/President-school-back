let baseUrl = window.location.origin;

function login(){
    let form = $('#form')[0];
    let dataForm = new FormData(form);
    $.ajax({
        url: baseUrl + "/auth/login",
        type: 'POST',
        enctype: 'application/json',
        data: dataForm,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            console.log(data)
            if(data['statusCode'] === 200){
                let token = data.message;
                $.ajax({
                    url: baseUrl + '/api/admin/dashboard',
                    type: 'GET',
                    enctype: 'application/json',
                    headers: {"Authorization": "Bearer " + token},
                    success: function(data){
                        console.log('admin dashboard');
                        sessionStorage.setItem("Authorization", 'Bearer ' + token);
                        window.location = '/api/admin/dashboard';
                        // alert(data);
                        // $(location).attr('href', baseUrl + '/api/admin/dashboard')
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
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

function addTest(th){
    let form = $('#form')[0];
    let dataForm = new FormData(form);

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