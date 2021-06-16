<template>
<div >
  <div class="page-wrapper chiller-theme toggled">
    <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
      <i class="fas fa-bars"></i>
    </a>
    <navbar></navbar>
  <!-- sidebar-wrapper  -->
    <main-patient style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));"></main-patient>
  <!-- page-content" -->
</div>
 
</div>
  

</template>

<script>

import MainPatient from './MainPatient.vue';
import Navbar from './navbar.vue';
// import axios from 'axios';

export default {
name:'Content',
components:{
MainPatient,
Navbar
  },
  methods:{
  sidebarClose(){
    var closeSide=document.querySelector('.page-wrapper');
    closeSide.classList.remove('toggled')
  },
  sidebarOpen(){
    var openSide=document.querySelector('.page-wrapper');
    openSide.classList.add('toggled')
  },
},
}


</script>

<style scoped>
@import '../assets/css/content.css'; 
@import '../assets/css/login.css'; 






</style>