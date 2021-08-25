<template>
 <div class="container">
  <div class="row">
    <atom-spinner
            v-if="pagemodebtn==3"
            :animation-duration="1000"
            :size="60"
            :color="'#689F38'"
           />
    <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
      <div class="account-update">
        <form @submit.prevent="onSubmit()">
          <div class="d-flex justify-content-center">
            <img src="../assets/ss-logo.png" style="width:100px" alt="">
          </div>
          <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Mail adresi</label>
              <input 
                v-on:keyup.enter="onSubmit()"
                @input=$v.email.$touch()
                v-model="email"
                type="email" 
                :class="{ 'is-invalid': submitted && $v.email.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.email.$error" class="invalid-feedback">
                  <div v-if="!$v.email.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                  <div v-if="!$v.email.email"  id="emailHelp" class="form-text text-danger">lütfen geçerli bir eposta adresi giriniz</div>
                </div>
          </div>
          <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">Şifre</label>
              <input 
                @input=$v.password.$touch()
                v-model="password"
                type="password"
                :class="{ 'is-invalid': submitted && $v.password.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.password.$error" class="invalid-feedback">
                  <div v-if="!$v.password.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                  <!-- <div v-if="!$v.password.numeric" id="emailHelp" class="form-text text-danger">lütfen sayısal karakter giriniz</div> -->
                  <div v-if="!$v.password.minLength" id="emailHelp" class="form-text text-danger">Şifre 4 karakterden uzun olmalıdır</div>
                  <div v-if="!$v.password.maxLength" id="emailHelp" class="form-text text-danger">Şifre 15 karakterden kısa olmalıdır</div>
                </div>
          </div>
          <div class="loginbtn d-flex justify-content-center" >
            <button type="submit" class="btn btn-success w-100 mt-2 " v-if="pagemodebtn==0" >Giriş</button>
            <atom-spinner
            v-if="pagemodebtn==1"
            :animation-duration="1000"
            :size="60"
            :color="'#689F38'"
           />
          </div>
          <div class="d-flex justify-content-start mt-3">
            <a @click="goforgotPass()" style="cursor:pointer" class="text-success" >Hesabımı unuttum</a>
          </div>
          <div class="mt-2">
            <a @click="goRegister()" style="cursor:pointer" class="text-success">Hesabınız yok mu? Şimdi Kayıt Ol</a>
          </div>
        </form>
      </div>
    </div>
  </div> 
</div>
</template>

<script>
import {AtomSpinner} from 'epic-spinners'
import Vue from 'vue'
import {required,email,minLength,maxLength} from 'vuelidate/lib/validators'
import axios from 'axios'

export default {
  components: {
      AtomSpinner
  },
  data() {
    return {
      email:null,
      password:null,
      submitted:false,
      pagemode:4,
      pagemodetwo:7,
      control:true,
      errors:[],
      pagemodebtn:0
    }
  },

  validations: {
    email: {
      required,
      email
    },

    password: {
      required,
      minLength: minLength(4),
      maxLength: maxLength(15)
    }
  },
  mounted(){
  
  },
  methods: {

    
    goRegister(){
      // alert('merhaba');
      this.$emit("pagemodewasedited", this.pagemode)
    },

    goforgotPass(){
      this.$emit("pagemodewasedited",this.pagemodetwo);
    },

    onSubmit() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid) {
      return;
      } 
      this.pagemodebtn=1;
      let form = {
        "email" : this.email,
        "password" : this.password
      }
      var url='http://api.smartsense.com.tr/api/Auth/login'
      axios.post(url, form, {
      }).then(res => {
          console.log(res);
        if(res.data.code == '400') {
          res.data.errors.forEach(element => {
            this.errors.push(element);
          });
          for(var i=0 ; i<this.errors.length ; i++)
            Vue.$toast.warning(this.errors[i]);
          }
          else if(res.status=='400') {
            this.$router.push('/error');
          } 
          else if(res.data.code=='200' && res.data.roleId==1) {
            localStorage.setItem('token',res.data.token);
            localStorage.setItem('namex',res.data.name);
            localStorage.setItem('surnamex',res.data.surname);
            localStorage.setItem('role',res.data.roleId)
            localStorage.setItem('userId',res.data.userId)
            localStorage.setItem('pageMode',1)
            // Vue.$toast.success('Giriş başarılı');
            Vue.$toast.success("giriş yapıldı")
            this.$router.push('/patienthome');
          }
          else if(res.data.code=='200' && res.data.roleId==2) {
            localStorage.setItem('token',res.data.token);
            localStorage.setItem('namex',res.data.name);
            localStorage.setItem('surnamex',res.data.surname);
            localStorage.setItem('role',res.data.roleId)
            localStorage.setItem('userId',res.data.userId)
            // Vue.$toast.success('Giriş başarılı');
            Vue.$toast.success("giriş yapıldı")
            this.$router.push('/doctorhome');
          }
          else if(res.data.code=='200' && res.data.roleId==3) {
            localStorage.setItem('token',res.data.token);
            localStorage.setItem('namex',res.data.name);
            localStorage.setItem('surnamex',res.data.surname);
            localStorage.setItem('role',res.data.roleId)
            localStorage.setItem('userId',res.data.userId)
            // Vue.$toast.success('Giriş başarılı');
            Vue.$toast.success("giriş yapıldı")
            this.$router.push('/supervisorhome');
          }
          else if(res.data.code=='200' && res.data.roleId==4) {
            localStorage.setItem('token',res.data.token);
            localStorage.setItem('name',res.data.name);
            localStorage.setItem('surname',res.data.surname);
            localStorage.setItem('role',res.data.roleId)
             localStorage.setItem('userId',res.data.userId)
            // Vue.$toast.success('Giriş başarılı');
            Vue.$toast.success("giriş yapıldı")
            this.$router.push('/adminhome');
          }
          else if(res.data.code=='200' && res.data.roleId==5) {
            localStorage.setItem('token',res.data.token);
            localStorage.setItem('namex',res.data.name);
            localStorage.setItem('surnamex',res.data.surname);
            localStorage.setItem('role',res.data.roleId)
            // Vue.$toast.success('Giriş başarılı');
            Vue.$toast.success("giriş yapıldı")
            this.$router.push('/companianhome');
          }
          this.pagemodebtn=0;
          this.errors.length=null
        })
    }
  }
}
</script>

<style scoped>
  @media screen and (min-width: 768px) {
    .account-update {
      width: 75%;
    }
  }
  @media screen and (max-width: 768px) {
    .account-update {
      width: 100%;
    }
  }
</style>

