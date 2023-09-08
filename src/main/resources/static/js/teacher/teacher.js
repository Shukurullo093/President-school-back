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

function test(){
    $.ajax({
        type: 'GET',
        url: '/api/admin/dashboard',
        beforeSend: function(xhr) {
            if (localStorage.token) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
            }
        },
        success: function(data) {
            alert('Hello ' + data.name + '! You have successfully accessed to /api/profile.');
        },
        error: function() {
            alert("Sorry, you are not logged in.");
        }
    });
};

function createLesson(){
    let form = $('#form')[0];
    let data = new FormData(form);

    $.ajax({
        url: baseUrl + "/api/teacher/rest/add/lesson",
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
                    // form.reset();
                    window.location = "/api/teacher/add/lesson";
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

// (() => {
//     const counter = (() => {
//         const input = document.getElementById('testAnswer'),
//         display = document.getElementById('answer'),
//         changeEvent = (evt) => display.innerHTML = evt.target.value.length + 1 + '-javobni kiriting',
//         getInput = () => input.value,
//         countEvent = () => input.addEventListener('keyup', changeEvent),
//         init = () => countEvent();
//
//         return {
//             init: init
//         }
//
//     })();
//
//     counter.init();
//
// })();