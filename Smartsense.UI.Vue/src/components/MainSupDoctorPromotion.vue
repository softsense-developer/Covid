<template>

<main class="page-content">
<div id="snackbar">Some text some message..</div>
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
          <form  @submit.prevent="PromotionDoctor(userID)">
            <div class="card-text mt-2" style="margin-top:3%">
              <span class="spanım">Kullanıcı ID Numarası</span>
              <input type="text" class="form-control" v-model="userID" id="exampleInputPassword1"  :class="{ 'is-invalid': submitted && $v.userID.$error }"   >
              <div v-if="submitted && $v.userID.$error" class="invalid-feedback">
                <span v-if="!$v.userID.required">ID Numarası Boş Olamaz</span>
                <span v-if="!$v.userID.numeric"><br>Lütfen Sayısal Karakter giriniz</span>
              </div>
            </div>
             <div class="form-group mt-5 d-flex justify-content-center">
                <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Ekle</button>
             </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

</template>

<script>
import axios from 'axios'
import { required,numeric } from "vuelidate/lib/validators";
export default {
    name:'mainsupdoktorpromotion',

    data() {
        return {
          userID:'',
          submitted: false
        }
    },
     validations:{
        userID:{required,numeric}
        },


    methods: {
        Toast(messages) {
         var x = document.getElementById("snackbar");
        x.className = "show";
       x.style.backgroundColor='rgb(15,120,108)';
       x.innerHTML=`<p style='color:white;'>${messages}</p>`
       setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
       },
        PromotionDoctor(id) {
            this.submitted = true;
            this.$v.$touch();
            if (this.$v.$invalid) {
              return;
            }
          console.log(id);
           var url='http://api.smartsense.com.tr/api/supervisor/doctorpromotion'+`/${id}`
           var token=localStorage.getItem('token');
          axios.get(url, {
           headers: {'Authorization': `Bearer ${token}`}
           }).then(res=> {
           console.log(res);
            if(res.data.code==200){
            this.Toast(res.data.message)
        }
        });

       }
       
      
     
    },


}
</script>

<style>

</style>