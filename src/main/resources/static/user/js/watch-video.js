let baseUrl = window.location.origin;

function checkTask(th){
   let id = $(th).attr('data-id');
   let form = $('#form' + id)[0];
   let data = new FormData(form);

   $.ajax({
      url: baseUrl + "/api/student/rest/check-task/" + id,
      type: 'POST',
      enctype: 'multipart/form-data',
      data: data,
      processData: false,
      contentType: false,
      cache: false,
      success: function (data) {
         if (data.statusCode === 200){
            $('#form' + id + ' .alert').css('display', 'block');
            $('#form' + id + ' .alert').css('background-color', 'green');
            $('#form' + id + ' .alert .alert-txt').text(data.message);
         } else {
            $('#form' + id + ' .alert').css('display', 'block');
            $('#form' + id + ' .alert').css('background-color', 'red');
            $('#form' + id + ' .alert .alert-txt').text(data.message);
            $('.vis' + id).css('display', 'block');
            setTimeout(function (){
               $('#form' + id + ' .alert').css('display', 'none');
               form.reset();
            }, 5000);
         }
      },
      error: function (e) {
         console.log(e);
      }
   })
}

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

toggleBtn.onclick = (e) =>{
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