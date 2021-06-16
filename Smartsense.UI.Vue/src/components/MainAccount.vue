<template>
  <div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
       <div class="container" >
         <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Şifre İşlemleri</h5>
          </div>
        </div>
      <div class="row d-flex justify-content-start rows" >
            <!-- .xa mobil kısım için dikkat et  -->
            <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5">
              <div class="account-update">
                
                <div class="form-container">
                  <div class="form-inner">
                    <form @submit.prevent="handleSubmit()">
                      <div class="message">
                        <div class="alert alert-success" role="alert" v-if="flashmessage">
                          Şifreniz Başarı bir şekilde Güncellendi.
                        </div>
                      </div>
                    
                      <div class="form-group">
                        <label for="password">Eski Şifre</label>
                        <input type="password" v-model="user.oldpassword" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.user.oldpassword.$error }" />
                        <div v-if="submitted && $v.user.oldpassword.$error" class="invalid-feedback">
                          <span v-if="!$v.user.oldpassword.required">Eski Şifre Boş Olamaz</span>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="password">Yeni Şifre</label>
                        <input type="password" v-model="user.password" id="password" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.user.password.$error }" />
                        <div v-if="submitted && $v.user.password.$error" class="invalid-feedback">
                          <span v-if="!$v.user.password.required">Yeni Şifre Boş Olamaz</span>
                          <span v-if="!$v.user.password.minLength">Şifre 6 karakterden az olamaz</span>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="confirmPassword">Yeni Şifre Tekrar</label>
                        <input type="password" v-model="user.confirmPassword" id="confirmPassword" name="confirmPassword" class="form-control" :class="{ 'is-invalid': submitted && $v.user.confirmPassword.$error }" />
                        <div v-if="submitted && $v.user.confirmPassword.$error" class="invalid-feedback">
                          <span v-if="!$v.user.confirmPassword.required">Yeni Şifre Tekrar Boş Olamaz</span>
                          <span v-else-if="!$v.user.confirmPassword.sameAsPassword">Yeni şifre ile aynı olmalı</span>
                        </div>
                      </div>
                      <div class="form-group mt-5 d-flex justify-content-center">
                        <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Şifremi Güncelle</button>
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
import { required, minLength, sameAs } from "vuelidate/lib/validators";


export default {
name:"MainAccount",

data(){
  return{
    user:{
    oldpassword:"",
    password: "",
    confirmPassword: ""
    },
    submitted: false,
    flashmessage:false,
    flashmessagex:false,
    errors:[],
  };
},

validations:{
  user:{
    oldpassword:{required, minLength: minLength(6)},
    password: { required, minLength: minLength(6) },
    confirmPassword: { required, sameAsPassword: sameAs('password') }
  }
},

methods:{
  handleSubmit(){
    this.submitted = true;
    // stop here if form is invalid
    this.$v.$touch();
    if (this.$v.$invalid){
      return;
    }
    var url='http://api.smartsense.com.tr/api/auth/changepassword'
    var token=localStorage.getItem('token');
    var user={
    "oldPassword":this.user.oldpassword,
    "newPassword": this.user.password,
    "confirmPassword": this.user.confirmPassword
    }
    console.log(user);
    axios.post(url,user,{
      headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
    console.log(res);
    if(res.data.errors.length==''){
      var messagex=document.querySelector('.message');
      var htmlx=`
        <div class="alert alert-success" role="alert" v-if="flashmessage">
          Şifre Değişiminiz başarılı
        </div>`
      messagex.innerHTML=htmlx;
      setTimeout(() => messagex.innerHTML='', 3000);
    } else {
      var message=document.querySelector('.message');
      var html=`
        <div class="alert alert-danger" role="alert" v-if="flashmessage">
          ${res.data.errors}
        </div>`
      message.innerHTML=html;
      setTimeout(() => message.innerHTML='', 3000);
    }
      })
    // alert("SUCCESS!! :-)\n\n" + JSON.stringify(this.user));
    
    console.log("hello");
    }
  }
};
</script>

<style>
@import '../assets/css/content.css'; 
@import '../assets/css/accountForm.css';

</style>