<template>
  <div >
  <div class="page-wrapper chiller-theme toggled" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
    <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
      <i class="fas fa-bars"></i>
    </a>
    <navbar></navbar>
  <!-- sidebar-wrapper  -->
    <main-sup-doctors style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));"></main-sup-doctors>
  <!-- page-content" -->
</div>
 
</div>
</template>

<script>
import MainSupDoctors from './MainSupDoctors.vue';
import Navbar from './navbar.vue';
export default {
  name:'SidebarProfile',
  components:{
      Navbar,
      MainSupDoctors,
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