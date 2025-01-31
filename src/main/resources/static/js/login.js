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
            if(data['statusCode'] === 200){
                let token = data.token;
                document.cookie = "Authorization=Bearer " + token;
                // $.ajax({
                //     url: baseUrl + data.message,
                //     type: 'GET',
                //     headers: {'Authorization': 'Bearer ' + token},
                //     enctype: 'application/json',
                //     data: null,
                //     processData: false,
                //     contentType: false,
                //     cache: false,
                //     success: function (data) {
                //
                //     },
                //     error: function (e) {
                //         console.log(e);
                //     }
                // })
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