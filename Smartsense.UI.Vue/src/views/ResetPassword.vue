<template>
  <div>
   
       <div class="container mt-5" >
      <div class="row d-flex justify-content-start rows" >
        <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid #607D8B">
            <h5 style="color:#607D8B;text-transform: capitalize;">Şifre Resetleme</h5>
          </div>
            <!-- .xa mobil kısım için dikkat et  -->
            <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5">
              <div class="account-update">
                
                <div class="form-container">
                  <div class="form-inner">
                    <form @submit.prevent="handleSubmit()">
                    <div class="alert alert-success" role="alert" v-if="flashmessage">
                   Şifreniz Başarı bir şekilde Güncellendi.
                    </div>
                      <div class="form-group">
                        <label for="password">Yeni Şifre</label>
                        <input type="password" v-model="user.password" id="password" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.user.password.$error }" />
                        <div v-if="submitted && $v.user.password.$error" class="invalid-feedback">
                          <span v-if="!$v.user.password.required">Yeni Şifre Boş Olamaz</span>
                          <span v-if="!$v.user.password.minLength">Password must be at least 6 characters</span>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="confirmPassword">Yeni Şifre Tekrar</label>
                        <input type="password" v-model="user.confirmPassword" id="confirmPassword" name="confirmPassword" class="form-control" :class="{ 'is-invalid': submitted && $v.user.confirmPassword.$error }" />
                        <div v-if="submitted && $v.user.confirmPassword.$error" class="invalid-feedback">
                          <span v-if="!$v.user.confirmPassword.required">Yeni Şifre Tekrar Boş Olamaz</span>
                          <span v-else-if="!$v.user.confirmPassword.sameAsPassword">Passwords must match</span>
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
    password: "",
    confirmPassword: "",
    guid:""
    },
    submitted: false,
    flashmessage:false
  };
},

validations:{
  user:{
     password: { required, minLength: minLength(6) },
    confirmPassword: { required, sameAsPassword: sameAs('password') }
  }
},
mounted(){
   this.user.guid =this.$route.params.guid
},

methods:{
    
  handleSubmit(){
    this.submitted = true;
    // stop here if form is invalid
    this.$v.$touch();
    if (this.$v.$invalid){
      return;
    }
    var url='http://api.smartsense.com.tr/api/auth/passwordreset'
    var user={
    "password": this.user.password,
    "confirmPassword": this.user.confirmPassword,
    "guid":this.user.guid
    }
    console.log(user);
    axios.post(url,user,{
    }).then(res=>{
    console.log(res);
      this.flashmessage=true;
      setTimeout(() => this.flashmessage = false, 3000);
      
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