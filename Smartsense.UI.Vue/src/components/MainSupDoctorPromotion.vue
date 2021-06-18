<template>

<main class="page-content">
  <div class="container">
    <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Doktor Atama</h5>
          </div>
        </div>
    <div class="row mt-5">
      <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        
        
        <div class="card" style="width: 100%;" >
          <div class="card-body" >
            <div class="card-text mt-2" style="margin-top:3%">
              <span class="spanım">Kullanıcı ID Numarası</span>
              <input type="text" class="form-control" v-model="userID" id="exampleInputPassword1"   >
            </div>
             <div class="card-text text-right mt-3 d-flex justify-content-between">
              <a  class="btn text-white mt-3 w-100 shadow" @click=" PromotionDoctor(userID)">Ekle</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

</template>

<script>
import axios from 'axios'
export default {
    name:'mainsupdoktorpromotion',

    data() {
        return {
          userID:''
        }
    },

    methods: {
        PromotionDoctor(id) {
          console.log(id);
           var url='http://api.smartsense.com.tr/api/supervisor/doctorpromotion'+`/${id}`
           var token=localStorage.getItem('token');
          axios.get(url, {
           headers: {'Authorization': `Bearer ${token}`}
           }).then(res=> {
           console.log(res);
        });

       }
       
      
     
    },


}
</script>

<style>

</style>