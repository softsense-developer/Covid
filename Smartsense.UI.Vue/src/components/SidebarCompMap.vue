<template>
<div >
  <div class="page-wrapper chiller-theme toggled" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
    <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
      <i class="fas fa-bars"></i>
    </a>
    <navbar></navbar>
  <!-- sidebar-wrapper  -->
    <main-comp-map style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));"></main-comp-map>
  <!-- page-content" -->
</div>
 
</div>
  

</template>

<script>

import MainCompMap from './MainMapComp';
import Navbar from './navbar.vue';
// import axios from 'axios';

export default {
name:'Content',
components:{
MainCompMap,
Navbar
  },
  methods:{
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