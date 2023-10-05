let baseUrl = window.location.origin;

$("#saveBtn").click(function (){
    let form = $('#form')[0];
    let dataForm = new FormData(form);
    if ($('textarea[name=questionTxt]').val().length > 0 || $('input[name=questionImg]').val().length > 0){
        if ($('textarea[name=v1]').val().length > 0 || $('input[name=v1img]').val().length > 0){
            if ($('textarea[name=v2]').val().length > 0 || $('input[name=v2img]').val().length > 0){
                if ($('textarea[name=v3]').val().length > 0 || $('input[name=v3img]').val().length > 0){
                    if ($('#saveBtn').text() === 'Saqlash'){
                        $.ajax({
                            url: baseUrl + "/api/admin/rest/add-iq-test",
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
                            url: baseUrl + "/api/admin/rest/edit-iq-test",
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
                } else {
                    $(".alert-danger").css('display', 'block');
                    $("#error-alert").text("3-variant matnini kiriting yoki rasm yuklang.");
                    setTimeout(function(){
                        $(".alert-danger").css('display', 'none');
                        $("#error-alert").text("");
                    }, 4000);
                }
            } else {
                $(".alert-danger").css('display', 'block');
                $("#error-alert").text("2-variant matnini kiriting yoki rasm yuklang.");
                setTimeout(function(){
                    $(".alert-danger").css('display', 'none');
                    $("#error-alert").text("");
                }, 4000);
            }
        } else {
            $(".alert-danger").css('display', 'block');
            $("#error-alert").text("1-variant matnini kiriting yoki rasm yuklang.");
            setTimeout(function(){
                $(".alert-danger").css('display', 'none');
                $("#error-alert").text("");
            }, 4000);
        }
    } else {
        $(".alert-danger").css('display', 'block');
        $("#error-alert").text("Savol matnini kiriting yoki rasm yuklang.");
        setTimeout(function(){
            $(".alert-danger").css('display', 'none');
            $("#error-alert").text("");
        }, 4000);
    }
});

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
    // $('.btn-primary').attr('data-test-id', $(this).attr("data-id"));
});