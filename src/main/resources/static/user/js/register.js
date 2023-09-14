let baseUrl = window.location.origin;

$(document).ready(function () {
   $("input[type=button]").click(function (){
      let form = $('#form')[0];
      let data = new FormData(form);
      if (data.get('pass').length < 8){
         $('#error').css('display', 'block');
         $("#error").text("Parol kamida 8 ta belgidan iborat bo'lishi kerak.");
         setTimeout(function(){
            $('#error').css('display', 'none');
            $("#error").text("");
         }, 5000);
      }
      if (data.get("pass") !== data.get("c_pass")){
         $('#error').css('display', 'block');
         $("#error").text("Parollar mos tushmadi. Qaytadan kiriting.");
         setTimeout(function(){
            $('#error').css('display', 'none');
            $("#error").text("");
         }, 5000);
      }
      else {
         $.ajax({
            url: baseUrl + "/api/student/rest/register",
            type: 'POST',
            enctype: 'multipart/form-data',
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
               if(data['statusCode'] === 201){
                  $('#error').css('display', 'block');
                  $("#error").text(data['message']);
                  setTimeout(function(){
                     $("#error").css('display', 'none');
                     $('#error').text('');
                     form.reset();
                     window.location = "/api/user/login";
                  }, 3000);
               }
               else {
                  $('#error').css('display', 'block');
                  $("#error").text(data['message']);
                  setTimeout(function(){
                     $("#error").css('display', 'none');
                     $('#error').text("");
                  }, 4000);
               }
            },
            error: function (e) {
               console.log(e);
            }
         })
      }
   });
});

let toggleBtn = document.getElementById('toggle-btn');
let body = document.body;
let darkMode = localStorage.getItem('dark-mode');

const enableDarkMode = () =>{
   toggleBtn.classList.replace('fa-sun', 'fa-moon');
   body.classList.add('dark');
   localStorage.setItem('dark-mode', 'enabled');
}

const disableDarkMode = () =>{
   toggleBtn.classList.replace('fa-moon', 'fa-sun');
   body.classList.remove('dark');
   localStorage.setItem('dark-mode', 'disabled');
}

if(darkMode === 'enabled'){
   enableDarkMode();
}

toggleBtn.onclick = () =>{
   darkMode = localStorage.getItem('dark-mode');
   if(darkMode === 'disabled'){
      enableDarkMode();
   }else{
      disableDarkMode();
   }
}

let sideBar = document.querySelector('.side-bar');

document.querySelector('#menu-btn').onclick = () =>{
   sideBar.classList.toggle('active');
   body.classList.toggle('active');
}

let profile = document.querySelector('.header .flex .profile');

document.querySelector('#user-btn').onclick = () =>{
   profile.classList.toggle('active');
   // search.classList.remove('active');
}

window.onscroll = () =>{
   profile.classList.remove('active');
   // search.classList.remove('active');

   if(window.innerWidth < 1200){
      sideBar.classList.remove('active');
      body.classList.remove('active');
   }
}