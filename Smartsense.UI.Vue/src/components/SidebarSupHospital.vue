<template>
  <div >
  <div class="page-wrapper chiller-theme toggled" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
    <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
      <i class="fas fa-bars"></i>
    </a>
    <navbar></navbar>
  <!-- sidebar-wrapper  -->
    <main-sup-hospital style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));"></main-sup-hospital>
  <!-- page-content" -->
</div>
 
</div>
</template>

<script>

import MainSupHospital from './MainSupHospital.vue';
import Navbar from './navbar.vue';
export default {
  name:'SidebarProfile',
  components:{
    Navbar,
    MainSupHospital
  },
   methods:{
  sidebarOpen(){
    var openSide=document.querySelector('.page-wrapper');
    openSide.classList.add('toggled')
  },
},
}
</script>

<style>

</style>