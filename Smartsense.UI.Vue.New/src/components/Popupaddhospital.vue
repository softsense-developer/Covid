<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="800px">
      <template v-slot:activator="{ on }">
        <v-btn outlined color="teal lighten-3" dark v-on="on">Hastane bilgisi ekle</v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Hastane bilgisi ekle</span>
        </v-card-title>
         <v-form class="px-3" ref="form">
        <v-card-text>
           <form @submit.prevent="onSubmit()">
          <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Hastane ismi</label>
              <input 
                
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
            <label for="exampleInputEmail1" class="form-label">Hastane Adres</label>
              <input 
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
            <label for="exampleInputPassword1" class="form-label">Hastane kapasitesi</label>
              <input 
                @input=$v.password.$touch()
                v-model="password"
                type="int"
                :class="{ 'is-invalid': submitted && $v.password.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.password.$error" class="invalid-feedback">
                  <div v-if="!$v.password.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                  <div v-if="!$v.password.numeric" id="emailHelp" class="form-text text-danger">lütfen sayısal karakter giriniz</div>
                  <!-- <div v-if="!$v.password.minLength" id="emailHelp" class="form-text text-danger">Kapasite en az 10 haneli olmalıdır</div> -->
                  <div v-if="!$v.password.maxLength" id="emailHelp" class="form-text text-danger">Şifre en fazla 6 haneli olmalıdır</div>
                </div>
          </div>
          
          <div class="d-flex justify-content-center">
            <button type="submit" class="btn btn-success w-100 mt-2">Hastane bilgisi ekle</button>
            


          </div>
        </form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog = false">Close</v-btn>
          <!-- <v-btn color="green" text outlined @click="submit">Save</v-btn> -->
        </v-card-actions>
         </v-form>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>

import Vue from 'vue'
import {required,numeric,maxLength} from 'vuelidate/lib/validators'
import axios from 'axios'
export default {
  data () {
    return {
      email:null,
      password:null,
      submitted:false,
      name:null,
      surname:null,
      dialog: false,
      errors:[],
    }
  },
    validations: {
    // email: {
    //    required,
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
      // minLength: minLength(10),
      maxLength: maxLength(10000)
    },
  },
    methods: {
      //todo: bu işlemde butonları öldürmelisin
      onSubmit() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid){
      return;
    }
      var token=localStorage.getItem('token');
      let form = 
        {
         "hospitalName": this.name,
         "address": this.surname,
         "hospitalCapacity": this.password
       }
      var url='http://api.smartsense.com.tr/api/admin/addhospital'
      axios.post(url,form,{
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
        if(res.data.code=='200') {
          Vue.$toast.success('hastane bilgileri başarıyla eklendi')
          // setTimeout(Vue.$toast.success('Hasta yakını başarılıyla eklendi'), 3000);
        }
        else if(res.data.code=='400') {
          res.data.errors.forEach(element => {
            this.errors.push(element);
          });
          for(var i=0 ; i<this.errors.length ; i++)
            Vue.$toast.warning(this.errors[i]);
        }
      })
    },
         formatDate (date) {
        if (!date) return null

        const [year, month, day] = date.split('-')
        return `${year}/${month}/${day}`
      },
      parseDate (date) {
        if (!date) return null

        const [year, month, day] = date.split('/')
        return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
      },
    },
    computed: {
      computedDateFormatted () {
        return this.formatDate(this.date)
      },
    },

    watch: {
      date () {
        this.dateFormatted = this.formatDate(this.date)
      },
    },


}
</script>