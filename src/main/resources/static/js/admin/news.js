let baseUrl = window.location.origin;

$("#saveBtn").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);
    // if (data.get("pass") !== data.get("repass")){
    //     $(".alert-danger").css('display', 'block');
    //     $("#error-alert").text("Parollar mos tushmadi. Qaytadan kiriting.");
    //     setTimeout(function(){
    //         $(".alert-danger").css('display', 'none');
    //         $("#error-alert").text("");
    //     }, 5000);
    // }
    // else {
        $.ajax({
            url: baseUrl + "/api/admin/rest/add/post",
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
    // }
});

$("#editBtn").click(function (){
    let form = $('#form')[0];
    let data = new FormData(form);
    $.ajax({
        url: baseUrl + "/api/admin/rest/edit/post/" + $('#editBtn').attr('data-id'),
        type: 'PUT',
        enctype: 'multipart/form-data',
        data: data,
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
});