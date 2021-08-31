<template>
 <div class="container">
  <div class="row">
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
          <button type="submit" class="btn btn-success w-100 mt-2">Gönder</button>
          <div class="d-flex justify-content-start mt-2">
            <a @click="geri()" style="cursor:pointer;" class="mt-4 text-success"><i class="fas fa-arrow-left"></i> Giriş yapmak için</a>
          </div>
        </form>
      </div>
    </div>
  </div> 
</div>
</template>

<script>
import axios from 'axios'
import {required,email} from 'vuelidate/lib/validators'

export default {
  data() {
    return {
      email:null,
      password:null,
      submitted:false,
      pagemode:4,
      pagemodetwo:3
    }
  },
  validations: {
    email: {
      required,
      email
    },
  },
  methods: {
    goRegister(){
      // alert('merhaba');
      this.$emit("pagemodewasedited", this.pagemode)
    },
    geri(){
      this.$emit("pagemodewasedited",this.pagemodetwo);
    },
    onSubmit() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid){
      return;
    }
      let form={
       email:this.email,
     }
     var url='http://api.smartsense.com.tr/api/auth/PasswordForgot'
     axios.post(url,form, {

     }).then(res=> {
       console.log(res);
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

