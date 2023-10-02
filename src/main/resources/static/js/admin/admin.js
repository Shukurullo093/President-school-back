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

