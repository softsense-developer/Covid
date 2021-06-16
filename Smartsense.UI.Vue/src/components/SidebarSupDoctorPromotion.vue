<template>
  <div >
  <div class="page-wrapper chiller-theme toggled" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
    <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
      <i class="fas fa-bars"></i>
    </a>
    <navbar></navbar>
  <!-- sidebar-wrapper  -->
    <main-sup-doctor-promotion style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));"></main-sup-doctor-promotion>
  <!-- page-content" -->
</div>
 
</div>
</template>

<script>

import MainSupDoctorPromotion from './MainSupDoctorPromotion.vue';
import Navbar from './navbar.vue';
export default {
  name:'SidebarDoctorPromotion',
  components:{
    Navbar,
    MainSupDoctorPromotion
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