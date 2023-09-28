let baseUrl = window.location.origin;
let dataIdBtn = 1;

$(".fa-paper-plane").click(function (){
   let form = $('#form')[0];
   let data = new FormData(form);

   $.ajax({
      url: baseUrl + "/api/student/rest/send/" + $('.fa-paper-plane').attr('data-lesson-id')  + "/" + $('.fa-paper-plane').attr('data-id'),
      type: 'POST',
      enctype: 'multipart/form-data',
      data: data,
      processData: false,
      contentType: false,
      cache: false,
      success: function () {
         form.reset();
         // refresh();
      },
      error: function (e) {
         console.log(e);
      }
   })
});

function taskBtn(th){
   $('button[data-id=' + dataIdBtn + ']').attr('class', 'btn');
   dataIdBtn = $(th).attr('data-id');
   $('.fa-paper-plane').attr('data-id', dataIdBtn);
   $(th).attr('class', 'option-btn');
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