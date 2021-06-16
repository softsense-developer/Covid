<template>
  <div>
    <sidebar></sidebar>
    <div class="container" >
        <div class="row d-flex justify-content-start rows  " style="padding-left:250px;">
          <div class="col-sm-12 col-md-12 col-lg-12 mt-3 d-flex justify-content-start">
          <router-link to="/About" tag="li" style="cursor:pointer;color:black;list-style-type:none;">Kişisel Bilgiler |</router-link>
            <router-link to="/About/Update" tag="li" style="cursor:pointer;color:black;list-style-type:none;" class="ml-2">İletişim Bilgilerimi Güncelle</router-link>
        </div>
           
            </div>
          </div>
        </div>
</template>

<script>

import Sidebar from '../components/Sidebar.vue'
export default {
    name:'Islem',
    components:{
        Sidebar
    }

}
</script>

<style>

</style>