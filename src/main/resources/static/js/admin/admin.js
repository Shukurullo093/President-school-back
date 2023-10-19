let baseUrl = window.location.origin;

$("#saveBtn").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);
    if (data.get("pass") !== data.get("repass")){
        $(".alert-danger").css('display', 'block');
        $("#error-alert").text("Parollar mos tushmadi. Qaytadan kiriting.");
        setTimeout(function(){
            $(".alert-danger").css('display', 'none');
            $("#error-alert").text("");
        }, 5000);
    }
    else {
        $.ajax({
            url: baseUrl + "/api/admin/rest/add/employee",
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
                        // window.location = "/api/admin/add/employee";
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
});

$("#editBtn").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);
    if (data.get("pass") !== data.get("repass")){
        $(".alert-danger").css('display', 'block');
        $("#error-alert").text("Parollar mos tushmadi. Qaytadan kiriting.");
        setTimeout(function(){
            $(".alert-danger").css('display', 'none');
            $("#error-alert").text("");
        }, 5000);
    }
    else {
        $.ajax({
            url: baseUrl + "/api/admin/rest/update/employee/" + $('#editBtn').attr('data-id'),
            type: 'POST',
            enctype: 'multipart/form-data',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data){
                if(data['statusCode']===200){
                    $(".alert-success").css('display', 'block');
                    $(".alert-success .text-dark").text(data['message']);
                    setTimeout(function(){
                        $(".alert-success").css('display', 'none');
                        $(".alert-success .text-dark").text('');
                        form.reset();
                        // window.location = "/api/admin/add/employee";
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
});

$("#editAdminBtn").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);
    if (data.get("pass") !== data.get("repass")){
        $(".alert-danger").css('display', 'block');
        $("#error-alert").text("Parollar mos tushmadi. Qaytadan kiriting.");
        setTimeout(function(){
            $(".alert-danger").css('display', 'none');
            $("#error-alert").text("");
        }, 5000);
    }
    else {
        $.ajax({
            url: baseUrl + "/api/admin/rest/update/admin",
            type: 'POST',
            enctype: 'multipart/form-data',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data){
                if(data['statusCode']===200){
                    $(".alert-success").css('display', 'block');
                    $(".alert-success .text-dark").text(data['message']);
                    setTimeout(function(){
                        $(".alert-success").css('display', 'none');
                        $(".alert-success .text-dark").text('');
                        form.reset();
                        // window.location = "/api/admin/add/employee";
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
});

$("#deleteBtn").click(function (){
    if (confirm("Hodim o'chirilsinmi?")){
        $.ajax({
            url: baseUrl + "/api/admin/rest/delete/employee/" + $('#deleteBtn').attr('data-id'),
            type: 'DELETE',
            enctype: 'application/json',
            // data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data){
                if(data['statusCode']===200){
                    window.location = "/api/admin/list/employee";
                    // $(".alert-success").css('display', 'block');
                    // $(".alert-success .text-dark").text(data['message']);
                    // setTimeout(function(){
                    //     $(".alert-success").css('display', 'none');
                    //     $(".alert-success .text-dark").text('');
                    //     form.reset();
                    //     //
                    // }, 4000);
                }
                else {
                    console.log(data['message']);
                    // $(".alert-danger").css('display', 'block');
                    // $("#error-alert").text();
                    // setTimeout(function(){
                    //     $(".alert-danger").css('display', 'none');
                    //     $("#error-alert").text("");
                    // }, 4000);
                }
            },
            error: function (e) {
                console.log(e);
            }
        })
    }
});

$("#deleteBtn1").click(function (){
    if (confirm("Hodim o'chirilsinmi?")){
        $.ajax({
            url: baseUrl + "/api/admin/rest/delete/employee/" + $('#deleteBtn1').attr('data-id'),
            type: 'DELETE',
            enctype: 'application/json',
            // data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data){
                if(data['statusCode']===200){
                    window.location = "/api/admin/list/employee";
                    // $(".alert-success").css('display', 'block');
                    // $(".alert-success .text-dark").text(data['message']);
                    // setTimeout(function(){
                    //     $(".alert-success").css('display', 'none');
                    //     $(".alert-success .text-dark").text('');
                    //     form.reset();
                    //     //
                    // }, 4000);
                }
                else {
                    console.log(data['message']);
                    // $(".alert-danger").css('display', 'block');
                    // $("#error-alert").text();
                    // setTimeout(function(){
                    //     $(".alert-danger").css('display', 'none');
                    //     $("#error-alert").text("");
                    // }, 4000);
                }
            },
            error: function (e) {
                console.log(e);
            }
        })
    }
});

$('#addStudent').click(function (){
    let form = $('#modal-form')[0];
    let data = new FormData(form);

    $.ajax({
        url: baseUrl + "/api/admin/rest/add/student",
        type: 'POST',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if(data['statusCode']===200){
                $(".msg-div").css('display', 'block');
                $('.msg-div').css('background-color', 'green')
                $(".msg").text(data['message']);
                setTimeout(function(){
                    $(".msg-div").css('display', 'none');
                    $(".msg").text('');
                    form.reset();
                }, 4000);
            }
            else {
                $(".msg-div").css('display', 'block');
                $('.msg-div').css('background-color', 'red')
                $(".msg").text(data['message']);
                setTimeout(function(){
                    $(".msg-div").css('display', 'none');
                    $(".msg").text("");
                }, 4000);
            }
        },
        error: function (e) {
            console.log(e);
        }
    })
})

function getStudent(th){
    $.ajax({
        url: baseUrl + "/api/admin/rest/get/student/" + $(th).attr('data-id'),
        type: 'GET',
        enctype: 'application/json',
        data: null,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            $('#exampleModalCenter1 .col-lg-4 img').attr('src', data.imagePath);
            $('.modal-student-name').text(data.fullName);
            $('.modal-student-phone').text(data.phone);
            $('.modal-student-gender').text(data.gender);
            $('.modal-student-grade').text(data.grade);
            $('.modal-student-date').text(data.createdDate);
            $("#modal-student-science").html(data.accessCourseDto)
        },
        error: function (e) {
            console.log(e);
        }
    })
}

function setPermit(th){
    $.ajax({
        url: baseUrl + "/api/admin/rest/set-permission-to-course/" + $(th).attr('data-id') + "/" + $(th).attr('data-course'),
        type: 'POST',
        enctype: 'application/json',
        data: null,
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            getStudent(th);

        },
        error: function (e) {
            console.log(e);
        }
    })
}


