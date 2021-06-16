<template>
   <div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container" >
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Refakatçı Ekle</h5>
          </div>
        </div>
        <div class="row d-flex justify-content-start rows" >
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5" >
            <div class="account-update" >
              <div class="form-container">
                <div class="form-inner">
                  <form  class="login" @submit.prevent="Update()">
                    <div class="form-group ">
                      <label>İsminiz</label>
                      <input type="text" v-model="name" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.name.$error }" />
                      <div v-if="submitted && $v.name.$error" class="invalid-feedback">
                        <span v-if="!$v.name.required">İsim Boş Olamaz</span>
                        <span v-if="!$v.name.minLength">İsim minumum 2 maksimum 40 karakter olabilir</span>
                        <span v-if="!$v.name.maxLength">İsim  maksimum 40 karakter olabilir</span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label>Soy isminiz</label>
                      <input type="text" v-model="surname" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.surname.$error }" />
                      <div v-if="submitted && $v.surname.$error" class="invalid-feedback">
                        <span v-if="!$v.surname.required">Soyisim  Boş Olamaz</span>
                        <span v-if="!$v.surname.minLength">Soyisim minimum 2 maksimum 40 karakter olmalıdır</span>
                        <span v-if="!$v.surname.maxLength">Soyisim  maksimum 40 karakter olabilir</span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label>Email</label>
                      <input  type="email" v-model="email" id="password" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.email.$error }" />
                      <div v-if="submitted && $v.email.$error" class="invalid-feedback">
                        <span v-if="!$v.email.required">Mail alanı boş olamaz </span>
                        <span v-if="!$v.email.email">E-posta geçersiz </span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label>Şifreniz</label>
                      <input type="password" v-model="password" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.password.$error }" />
                        <div v-if="submitted && $v.password.$error" class="invalid-feedback">
                          <span v-if="!$v.password.required">Şifre alanı boş olamaz</span>
                          <span v-if="!$v.password.minLength">Şifre minimum 6 karater olmalı </span>
                          <span v-if="!$v.password.maxLength">Şifre maksimum 50 karater olabilir </span>
                        </div>
                    </div>
                    <div class="form-group mt-5 d-flex justify-content-center">
                      <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Ekle</button>
                    </div>
                    <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;">
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
      
  </div>
</template>

<script>
import axios from 'axios'
import { required,email,minLength,maxLength } from "vuelidate/lib/validators";
export default {
name:"infoUpdate",
data(){
  return{
  name:"",
  surname:"",
  password:"",
  email:"",
  flashmessage:false,
   submitted: false
  }
},
mounted() {
  
},
validations:{
  name:{required,
  minLength: minLength(2),
  maxLength: maxLength(40)
  },
  surname: { required,
    minLength: minLength(2),
    maxLength: maxLength(40)    
  },
  password: { required, 
  minLength: minLength(6) ,
  maxLength: maxLength(50) 
  },
  email:{required,email}
  
},

methods:{
  Update(){
    this.submitted = true;
    // stop here if form is invalid
    this.$v.$touch();
    if (this.$v.$invalid){
      return;
    }
    var url='http://api.smartsense.com.tr/api/patient/addcompanion'
    var token=localStorage.getItem('token');
    var JsonObjectsx={
      "name": this.name,
      "surname": this.surname,
      "password": this.password,
      "email": this.email,

   }
    console.log(JsonObjectsx);
    axios.post(url,JsonObjectsx,{
     headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
        console.log(res);
        this.flashmessage=true;
         setTimeout(() => this.flashmessage = false, 3000);
    })
  },
  
  
  
}

}

</script>

<style>

</style>