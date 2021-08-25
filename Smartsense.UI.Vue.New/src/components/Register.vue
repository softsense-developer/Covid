<template>
 <div class="container">
  <div class="row">
    <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
      <div class="account-update" v-if="pagemode==4">
        <form @submit.prevent="onSubmit()">
          <div class="d-flex justify-content-center">
            <img src="../assets/ss-logo.png" style="width:100px" alt="">
          </div>
          <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">İsim</label>
              <input 
                v-on:keyup.enter="onSubmit()"
                @input=$v.name.$touch()
                v-model="name"
                type="text" 
                :class="{ 'is-invalid': submitted && $v.name.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.name.$error" class="invalid-feedback">
                  <div v-if="!$v.name.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
          </div>
          <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Soyisim</label>
              <input 
                v-on:keyup.enter="onSubmit()"
                @input=$v.surname.$touch()
                v-model="surname"
                type="text" 
                :class="{ 'is-invalid': submitted && $v.surname.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.surname.$error" class="invalid-feedback">
                  <div v-if="!$v.surname.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>                </div>
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
                  <div v-if="!$v.password.numeric" id="emailHelp" class="form-text text-danger">lütfen sayısal karakter giriniz</div>
                  <div v-if="!$v.password.minLength" id="emailHelp" class="form-text text-danger">Şifre 4 karakterden uzun olmalıdır</div>
                  <div v-if="!$v.password.maxLength" id="emailHelp" class="form-text text-danger">Şifre 10 karakterden kısa olmalıdır</div>
                </div>
          </div>
          <div class="d-flex justify-content-center">
            <button type="submit" class="btn btn-success w-100 mt-2" v-if="pagemodebtn==0">Kayıt ol</button>
            
              <atom-spinner
            v-if="pagemodebtn==1"
            :animation-duration="1000"
            :size="60"
            :color="'#689F38'"
           />

          </div>
          
          <div class="d-flex justify-content-start">
            <a @click="geri()" style="cursor:pointer;" class="mt-4 text-success"><i class="fas fa-arrow-left"></i> Giriş yapmak için</a>
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
import {required,email,numeric,minLength,maxLength} from 'vuelidate/lib/validators'
import axios from 'axios'

export default {
  props : ["pagemode"],
  components: {
      AtomSpinner
  },
  data() {
    return {
      email:null,
      password:null,
      submitted:false,
      name:null,
      surname:null,
      pagemodex:0,
      pagemodebtn:0,
      errors:[]
    }
  },
  validations: {
    email: {
      required,
      email
    },
    name: {
      required,
    },
    surname: {
      required
    },
    password: {
      required,
      numeric,
      minLength: minLength(4),
      maxLength: maxLength(10)
    }
  },
  methods: {
    geri(){
      this.$emit("pagemodewasedited", this.pagemodex)
    },
    onSubmit() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid){
      return;
    }
    this.pagemodebtn=1;
      let form = {
        "email":this.email,
        "password":this.password,
        "name":this.name,
        "surname":this.surname
      }
      var url='http://api.smartsense.com.tr/api/Auth/register'
      axios.post(url,form,{
      }).then(res=>{
        if(res.data.code=='200') {
          Vue.$toast.success('Hesabınız oluşturuldu');
        }
        else if(res.data.code=='400') {
          res.data.errors.forEach(element => {
            this.errors.push(element);
          });
          for(var i=0 ; i<this.errors.length ; i++)
            Vue.$toast.warning(this.errors[i]);
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

