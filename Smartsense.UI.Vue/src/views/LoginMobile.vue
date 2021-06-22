<template>
 <div class="container">
     <div class="row">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex mt-5 justify-content-center">
            <scaling-squares-spinner class="text-info"
            :animation-duration="1000"
            :size="75"
            :color="'#0f796c'"
          />
          </div>
      </div>
  <div class="row">
    <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5 y">
      <div class="wrapper" v-if="pageMode==1" style="opacity:0;pointer-events:none;">
        <div class="title-text">
          <div class="title login">
            <img src="https://smartsense.com.tr/img/ss-logo.PNG" alt="" width="75px" height="75px">
          </div>
          <div class="title signup">
            <img src="https://smartsense.com.tr/img/ss-logo.PNG" alt="" width="75px" height="75px">
          </div>
        </div>
        <div class="form-container">
          <div class="slide-controls">
            <input type="radio" name="slide" id="login" checked>
            <input type="radio" name="slide" id="signup" @click="hello()">
            <label for="login" class="slide login">Giriş</label>
            <label for="signup" class="slide signup">Üye Ol</label>
            <div class="slider-tab"></div>
          </div>
          <div class="form-inner">
          <form  class="login">
            <div class="field">
              <input type="text" placeholder="Email Address" required v-model="user.email">
            </div>
            <div class="field">
            <input type="password" placeholder="Password" required v-model="user.password">
             <div class="alert alert-danger mt-3" role="alert" v-if="err.length!=''">
                   {{err[0]}}
                </div>
            </div>
            <div class="pass-link" style="margin-top:5rem;">
               <a class="back" @click="forgotPass()">Şifremi Unuttum?</a> 
            </div>
            <div class="field btn">
              <div class="btn-layer" ></div>
              <input value="Giriş" type="button" @click="logIn()" >
            </div>
            <div class="signup-link" @click="go()" style="cursor:pointer;">
            Hesabınız yok mu? Şimdi Kayıt Ol
            </div>
          </form>
          <!-- kayıt olma formunun validationları buraya gelecek -->
          <form @submit.prevent="handleSubmit()">
            
                <div class="form-group">
                        <input placeholder="İsim" type="text" v-model="userPass.name" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.userPass.name.$error }" />
                          <div v-if="submitted && $v.userPass.name.$error" class="invalid-feedback">
                            <span v-if="!$v.userPass.name.required">İsim alanı boş Olamaz</span>
                          </div>
                      </div>
                      <div class="form-group">
                        <input placeholder="Soyisim" type="text" v-model="userPass.surname" id="password" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.userPass.surname.$error }" />
                          <div v-if="submitted && $v.userPass.surname.$error" class="invalid-feedback">
                            <span v-if="!$v.userPass.surname.required">Soyisim alanı boş olamaz</span>
                          </div>
                    </div>
                    <div class="form-group">
                        <input placeholder="Email" type="email" v-model="userPass.email" id="password" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.userPass.email.$error }" />
                          <div v-if="submitted && $v.userPass.email.$error" class="invalid-feedback">
                            <span v-if="!$v.userPass.email.required">Mail alanı boş olamaz </span>
                            <span v-if="!$v.userPass.email.email">E-posta geçersiz </span>
                          </div>
                    </div>
                     <div class="form-group">
                        <input placeholder="Şifre" type="password" v-model="userPass.password" id="password" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.userPass.password.$error }" />
                          <div v-if="submitted && $v.userPass.password.$error" class="invalid-feedback">
                            <span v-if="!$v.userPass.password.required">Şifre alanı boş olamaz</span>
                            <span v-if="!$v.userPass.password.minLength">Şifre 6 karakterden küçük olamaz</span>
                          </div>
                      </div>
                      <div class="alert alert-success" role="alert" v-if="flashMessage==0">
                   Hesabınız Başarıyla oluşturuldu
                </div>
                <div class="alert alert-danger" role="alert" v-if="flashMessage==1">
                   Böyle bir Kullanıcı bulunmaktadır
                </div>
                      
                      <div class="form-group mt-5 d-flex justify-content-center">
                        <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Kaydet</button>
                      </div>
                    </form>      
          </div>
        </div>
      </div>
      <div class="wrapperx" style="width:400px" v-if="pageMode==2">
        <div class="title-text">
          <div class="title login">
            <img src="https://smartsense.com.tr/img/ss-logo.PNG" alt="" width="75px" height="75px">
          </div>
          <div class="title signup">
            <img src="https://smartsense.com.tr/img/ss-logo.PNG" alt="" width="75px" height="75px">
          </div>
        </div>
        <div class="form-container">
          <div class="form-inner">
            <!-- Şifremi unuttum validationları buraya gelecek -->
           <form @submit.prevent="signInButtonPressed">
                   <div class="form-group">
                        <input placeholder="Email Adres" type="email" v-model="reg.email" id="email" name="email" class="form-control">
                    </div>
                    <div class="form-group mt-5 d-flex justify-content-center">
                        <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;" @click="submitMail()">Şifre Gönder</button>
                    </div>
                    <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;">
                      <a class="backx" @click="returnLogin()"><i class="fas fa-arrow-left" style="font-size:20px;"></i> Geri Dön</a> 
                    </div>
          </form> 
         
          </div>
        </div>
      </div>
    </div>
  </div>
</div> 
</template>

<script>
import {ScalingSquaresSpinner} from 'epic-spinners'
import { required,email, minLength } from "vuelidate/lib/validators";
import axios from 'axios'
export default {
name: 'Login',
data(){
  return{
    err:[],
    pageMode:1,
    token:"",
    user: {
      email: '',
      password: ''
    },
    userPass:{
    name:"",
    surname:"",
    email:"",
    password: "",
    
    },
    page_type:'',
    page_typetwo:'',
     reg: {
      
       email:'',
     
     },
    flashMessage:2,
   
    resDizi:[],
    submitted: false,
    submittedPass:false
    
  }
},

validations:{
  userPass:{
    name:{required},
    surname:{required},
    email:{required,email},
    password: { required, minLength: minLength(6)},
 },
//  reg:{
//    email:{required,email}
//  }
 

  
},
mounted(){
  const queryString = window.location.search;
  console.log(queryString);
  const urlParams = new URLSearchParams(queryString);
  this.page_type = urlParams.get('email')
  this.page_typetwo = urlParams.get('pass')
  this.logIn(this.page_type,this.page_typetwo)
  
},
components:{
    ScalingSquaresSpinner
},
methods:{
    handleSubmit(){
    this.submitted = true;
    // stop here if form is invalid
    this.$v.$touch();
    if (this.$v.$invalid){
      return;
    }
    var url='http://api.smartsense.com.tr/api/auth/register'
    var user = {
    "name":this.userPass.name,
    "surname":this.userPass.surname,
    "email":this.userPass.email,
    "password": this.userPass.password
    }
    console.log(user);
    axios.post(url,user,{
    }).then(res=>{
    console.log(res);
    if(res.data.code == 200){
      this.flashMessage = 0
       setTimeout(() => this.flashMessage = 2, 3000);
    }else{
      this.flashMessage = 1
      setTimeout(() => this.flashMessage = 2, 3000); 
    }
    })
    // alert("SUCCESS!! :-)\n\n" + JSON.stringify(this.user));
    console.log("hello");
  },

  signInButtonPressed (e) {
    console.log("Sign In Button Pressed");
    e.preventDefault();
  },
  
  submitMail(){
    var url='http://api.smartsense.com.tr/api/auth/PasswordForgot'
    var user={
    "email":this.reg.email,
    
    }
    axios.post(url,user,{
    }).then(res=>{
    console.log(res);
    })
    // alert("SUCCESS!! :-)\n\n" + JSON.stringify(this.user));
    console.log("hello");
  },
  
 returnLogin(){
  this.pageMode=1
 },
 forgotPass() {
  this.pageMode=2;
  },



logIn(x,y) {
  var emailx=x;
  var passx=y;
  var url='http://api.smartsense.com.tr/api/auth/login';
  var jsonObject={
    "email":emailx,
    "password":passx
  }
  axios.post(url,jsonObject,{
  }).then(res=>{
    console.log(res);
    console.log(res.data.token);
    localStorage.setItem('token',res.data.token);
    localStorage.setItem('name',res.data.name);
    localStorage.setItem('roleId',res.data.roleId);
    localStorage.setItem('userId',res.data.userId);
    localStorage.setItem('surname',res.data.surname);
      if((localStorage.getItem('token')==res.data.token)&&(res.data.roleId==1)){
        this.$router.push('/HomePage');
      }else if((localStorage.getItem('token')==res.data.token)&&(res.data.roleId==2)){
        this.$router.push('/DoktorHome');
      }else if((localStorage.getItem('token')==res.data.token)&&(res.data.roleId==4)){
        this.$router.push('/AdminHome');
      }else if((localStorage.getItem('token')==res.data.token)&&(res.data.roleId==3)){
        this.$router.push('/HomeSupervisor');
        }else if((localStorage.getItem('token')==res.data.token)&&(res.data.roleId==5)){
        this.$router.push('/CompHome');
        }else{
        setTimeout(this.$router.push('/Login'), 3000);
        
      }
  })
},
go(){
  const signupBtn = document.querySelector("label.signup");
  signupBtn.click();
  return false;
},
hello(){
  const loginText = document.querySelector(".title-text .login");
  const loginForm = document.querySelector("form.login");
  const loginBtn = document.querySelector("label.login");
  loginForm.style.marginLeft = "-50%";
  loginText.style.marginLeft = "-50%";
  loginBtn.onclick = (()=>{
  loginForm.style.marginLeft = "0%";
  loginText.style.marginLeft = "0%";
});
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
@import '../assets/css/login.css';
.back{
  cursor: pointer;
}
.backx{
  cursor: pointer;
}



</style>
