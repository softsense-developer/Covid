<template>
  <div >
  <div class="page-wrapper chiller-theme toggled" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
    <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
      <i class="fas fa-bars"></i>
    </a>
    <navbar></navbar>
  <!-- sidebar-wrapper  -->
    <main-patient-notification style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));"></main-patient-notification>
  <!-- page-content" -->
</div>
 
</div>
</template>

<script>
import MainPatientNotification from './MainPatientNotification.vue';
import Navbar from './navbar.vue';
export default {
  name:'SidebarProfile',
  components:{
      Navbar,
      MainPatientNotification
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