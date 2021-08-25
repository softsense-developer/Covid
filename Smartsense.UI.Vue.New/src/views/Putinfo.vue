<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">Bilgilerimi güncelle</h1>
      <div class="container">
        <Accountwaydoc id="sidebar" class="d-flex justify-content-end"/>
      <div class="row">
        <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
            <div class="account-update">
                <form @submit.prevent="onSubmit()">
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
            <label for="exampleInputPassword1" class="form-label">Telefon</label>
              <input 
                @input=$v.password.$touch()
                v-model="password"
                type="tel"
                :class="{ 'is-invalid': submitted && $v.password.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.password.$error" class="invalid-feedback">
                  <div v-if="!$v.password.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                  <div v-if="!$v.password.numeric" id="emailHelp" class="form-text text-danger">lütfen sayısal karakter giriniz</div>
                  <div v-if="!$v.password.minLength" id="emailHelp" class="form-text text-danger">Telefon 11 rakamdan oluşmalıdır</div>
                  <div v-if="!$v.password.maxLength" id="emailHelp" class="form-text text-danger">Şifre 11 rakamdan oluşmalıdır</div>
                </div>
          </div>
          <div class="d-flex justify-content-center">
            <button type="submit" class="btn btn-success w-100 mt-2">Güncelle</button>
          </div>
          </form>
            </div>
          
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar'
import Accountwaydoc from '@/components/Accountwaydoc'
import {required,numeric,minLength,maxLength} from 'vuelidate/lib/validators'
import Vue from 'vue'
import axios from 'axios'
export default {
  name:'patientcompanian',
  data() {
    return {
      companians: [],
      password:null,
      submitted:false,
      name:null,
      surname:null,
    }
  },
  components :{
    Navbar,
    Accountwaydoc
  },
  validations: {
    // email: {
    //   required,
    //   email
    // },
    name: {
      required,
    },
    surname: {
      required
    },
    password: {
      required,
      numeric,
      minLength: minLength(11),
      maxLength: maxLength(11)
    }
  },
  mounted() {

    // this.getpatientcomp();
  },
  methods: {
    onSubmit() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid){
      return;
    }
      var token=localStorage.getItem('token');
      let form = {
        "phone":this.password,
        "name":this.name,
        "surname":this.surname
      }
      var url='http://api.smartsense.com.tr/api/auth/putinfo'
      axios.post(url,form, {
        headers:{'Authorization': `Bearer ${token}`},
      }).then(res=> {
        console.log(res);
        if(res.data.code=='200') {
        Vue.$toast.success("bilgileriniz başarıyla güncellendi");
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
    },
  }
}
</script>

<style scoped>

  .list-group {
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);
  }
  .card-header {
    background: rgb(104,187,104);
    color:white;
  }
  .account-update {
    overflow: hidden;
    width: 100%;
    background: #fff;
    padding: 30px;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);
  }

</style>


