<template>
  <div>
      <Navbar />
      <h1 class="subheading grey--text">Şifremi değiştir</h1>
       <div class="container" >
      <Accountwaydoc id="sidebar" class="d-flex justify-content-end"/>
      <div class="row d-flex justify-content-start rows" >
            <!-- .xa mobil kısım için dikkat et  -->
            <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5">
              <div class="account-update w-100">
                
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
                        <button type="submit" class="btn btn-success w-100 mt-2">Şifremi Güncelle</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div> 
            </div>
          </div>
        </div>
  </div>
</template>

<script>
import axios from 'axios'
import { required, minLength, sameAs } from "vuelidate/lib/validators";
import Navbar from '@/components/Navbar'
import Accountwaydoc from '@/components/Accountwaydoc'
import Vue from 'vue'
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
components :{
    Navbar,
    Accountwaydoc
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
    }).then(res=> {
        console.log(res);
        if(res.data.code=='200') {
        Vue.$toast.success("şifre başarıyla değiştirildi");
        }
        else if(res.data.code=='400') {
          res.data.errors.forEach(element => {
            this.errors.push(element);
          });
          for(var i=0 ; i<this.errors.length ; i++)
            Vue.$toast.warning(this.errors[i]);
        }
      }).catch ((err) => {
          Vue.$toast.error(err);
      })
    // alert("SUCCESS!! :-)\n\n" + JSON.stringify(this.user));
    
    console.log("hello");
    }
  }
};
</script>

<style scoped>

@import '../assets/css/login.css'; 
/* @import '../assets/css/accountForm.css'; */

</style>