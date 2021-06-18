<template>

  <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
    <div id="snackbar">Some text some message..</div>
    <div class="container">
      <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Hastane</h5>
          </div>
        </div>
        <div class="d-flex text-center justify-content-center" v-if="pageMode==3" style="margin-top:22%;">
      <scaling-squares-spinner class="text-info"
        :animation-duration="1000"
        :size="75"
        :color="'#0f796c'"
      />
    </div>
      <div class="row mt-5">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
          <div class="card" v-if="pageMode==0">
            <div class="container">
              <div class="row" >
                <div class="col-sm-6 col-md-6 col-lg-6 d-flex justify-content-start" >
                  <img src="../assets/hospital.jpg" id="person-img" alt="" />
                </div>
                <div class="col-sm-6 col-md-6 col-lg-6">
                  <h6 class="mt-3"><span style="color:rgb(28,134,122)">hastane:</span> {{hospitalName}}</h6>
                  <h6><span  style="color:rgb(28,134,122)">Adres:</span> {{address}}</h6>
                  <h6><span  style="color:rgb(28,134,122)">hastane kapasite:</span> {{capacity}}</h6>
                  <div class="text-right mb-3" style="margin-top:20%">
                    <button class="btn text-center w-100 text-white shadow" @click="pageModes()">Güncelle</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card" style="width: 100%;" v-if="pageMode==1">
            <div class="card-body" >
              <form @submit.prevent="Update()">
                <div class="form-group">
                  <label for="password">hastane ismi giriniz</label>
                  <input type="text" v-model="objects.hospitalName" id="hospitalName" name="hospitalName" class="form-control" :class="{ 'is-invalid': submitted && $v.objects.hospitalName.$error }" />
                  <div v-if="submitted && $v.objects.hospitalName.$error" class="invalid-feedback">
                    <span v-if="!$v.objects.hospitalName.required">hastane ismi  Boş Olamaz</span>
                  </div>
                </div>
                <div class="form-group">
                  <label for="password">hastane adresi giriniz</label>
                  <input type="text" v-model="objects.address" id="address" name="address" class="form-control" :class="{ 'is-invalid': submitted && $v.objects.address.$error }" />
                  <div v-if="submitted && $v.objects.address.$error" class="invalid-feedback">
                    <span v-if="!$v.objects.address.required">hastane adresi Boş Olamaz</span>
                  </div>
                </div>
                <div class="form-group">
                  <label for="password">hastane kapasitesi giriniz</label>
                  <input type="text" v-model="objects.capacity" id="capacity" name="capacity" class="form-control" :class="{ 'is-invalid': submitted && $v.objects.capacity.$error }" />
                  <div v-if="submitted && $v.objects.capacity.$error" class="invalid-feedback">
                    <span v-if="!$v.objects.capacity.required">hastane kapasitesi  Boş Olamaz</span>
                </div>
                </div>
              <div class="card-text text-right mt-3 d-flex justify-content-between">
                <i class="fas fa-arrow-left mt-4" style="font-size:15px;cursor:pointer" @click="pageModesx()"> Geri</i>
              </div>
                <div class="form-group mt-3 mb-3 d-flex justify-content-center">
                  <button class="btn  " style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Güncelle</button>
                </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>
</template>

<script>
import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
import { required } from "vuelidate/lib/validators";
export default {
    name:'mainsuphospital',

  data() {
    return {
      pageMode:0,
      objects: {
        hospitalName:'',
        address:'',
        capacity:''
      },
      hospitalName:'',
      address:'',
      capacity:'',

      submitted: false,
      flashmessage:false
    }
  },
    components: {
      ScalingSquaresSpinner
    },

    validations:{
      objects: {
        hospitalName:{required },
        address: { required},
        capacity: { required }
      }
    },

    methods: {

      Toast(messages) {
        // Get the snackbar DIV
        var x = document.getElementById("snackbar");
        // Add the "show" class to DIV
        x.className = "show";
        x.style.backgroundColor='rgb(15,120,108)';
        x.innerHTML=`<p style='color:white;'>${messages}</p>`
        // After 3 seconds, remove the show class from DIV
        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
      },

      pageModes(){
        this.pageMode=1
      },

      pageModesx(){
        this.pageMode=0
      },

      getDoctor(){
        var name=localStorage.getItem('name');
        var surname=localStorage.getItem('surname');
        return name+' '+surname;
      },

      getAllHospitals(){
        var url='http://api.smartsense.com.tr/api/supervisor/getsupervisorhospital'
        var token=localStorage.getItem('token');
        axios.get(url, {
          headers: {'Authorization': `Bearer ${token}`}
        }).then(res=> {
        console.log(res);
        this.hospitalName=res.data.hospitalName;
        this.address=res.data.address;
        this.capacity=res.data.hospitalCapacity;
        });
      },  

      Update() {
        this.submitted = true;
        this.$v.$touch();
        if (this.$v.$invalid) {
          return;
        }
        var url='http://api.smartsense.com.tr/api/supervisor/puthospital'
        var token=localStorage.getItem('token');
        var JsonObjectsx={
          "hospitalName": this.objects.hospitalName,
          "address": this.objects.address,
          "hospitalCapacity": this.objects.capacity
        }
        console.log(JsonObjectsx);
        axios.post(url,JsonObjectsx, {
          headers:{'Authorization': `Bearer ${token}`}
        }).then(res=>{
        console.log(res);
        this.flashmessage=true;
        setTimeout(() => this.flashmessage = false, 3000);
          if(res.data.code==200) {
            this.pageMode=3;
            this.Toast(res.data.message)
            setTimeout(() => {
              this.pageMode=0
            }, 2000);    
          }
          })
      },
    },

    mounted() {
      this.getAllHospitals();
    }
}
</script>

<style>
 /*
=============== 
Fonts
===============
*/
@import url("https://fonts.googleapis.com/css?family=Open+Sans|Roboto:400,700&display=swap");

/*
=============== 
Variables
===============
*/

:root {
  /* dark shades of primary color*/
  --clr-primary-1: hsl(205, 86%, 17%);
  --clr-primary-2: hsl(205, 77%, 27%);
  --clr-primary-3: hsl(205, 72%, 37%);
  --clr-primary-4: hsl(205, 63%, 48%);
  /* primary/main color */
  --clr-primary-5: hsl(205, 78%, 60%);
  /* lighter shades of primary color */
  --clr-primary-6: hsl(205, 89%, 70%);
  --clr-primary-7: hsl(205, 90%, 76%);
  --clr-primary-8: hsl(205, 86%, 81%);
  --clr-primary-9: hsl(205, 90%, 88%);
  --clr-primary-10: hsl(205, 100%, 96%);
  /* darkest grey - used for headings */
  --clr-grey-1: hsl(209, 61%, 16%);
  --clr-grey-2: hsl(211, 39%, 23%);
  --clr-grey-3: hsl(209, 34%, 30%);
  --clr-grey-4: hsl(209, 28%, 39%);
  /* grey used for paragraphs */
  --clr-grey-5: hsl(210, 22%, 49%);
  --clr-grey-6: hsl(209, 23%, 60%);
  --clr-grey-7: hsl(211, 27%, 70%);
  --clr-grey-8: hsl(210, 31%, 80%);
  --clr-grey-9: hsl(212, 33%, 89%);
  --clr-grey-10: hsl(210, 36%, 96%);
  --clr-white: #fff;
  --clr-red-dark: hsl(360, 67%, 44%);
  --clr-red-light: hsl(360, 71%, 66%);
  --clr-green-dark: hsl(125, 67%, 44%);
  --clr-green-light: hsl(125, 71%, 66%);
  --clr-black: #222;
  --ff-primary: "Roboto", sans-serif;
  --ff-secondary: "Open Sans", sans-serif;
  --transition: all 0.3s linear;
  --spacing: 0.1rem;
  --radius: 0.25rem;
  --light-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  --dark-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  --max-width: 1170px;
  --fixed-width: 620px;
}
ul {
  list-style-type: none;
}
a {
  text-decoration: none;
}
h1,
h2,
h3,
h4 {
  letter-spacing: var(--spacing);
  text-transform: capitalize;
  line-height: 1.25;
  margin-bottom: 0.75rem;
  font-family: var(--ff-primary);
}
h1 {
  font-size: 3rem;
}
h2 {
  font-size: 2rem;
}
h3 {
  font-size: 1.25rem;
}
h4 {
  font-size: 0.875rem;
}
h6{
  border-bottom: 1px solid black;
  text-transform: capitalize;
  padding-top: 3%;
}
.spanım{
  /* text-transform: uppercase; */
  font-size: 12px;
  text-transform: capitalize;
  color:rgb(129, 120, 120)
}
p {
  margin-bottom: 1.25rem;
  color: var(--clr-grey-5);
}
@media screen and (min-width: 800px) {
  h1 {
    font-size: 4rem;
  }
  h2 {
    font-size: 2.5rem;
  }
  h3 {
    font-size: 1.75rem;
  }
  h4 {
    font-size: 1rem;
  }
  body {
    font-size: 1rem;
  }
  h1,
  h2,
  h3,
  h4 {
    line-height: 1;
  }
}
/*  global classes */

/* section */
@media screen and (min-width: 992px) {
  
}

#person-img {
  width: 100%;
  display: block;
  height: 85%;
  object-fit: cover;
  border-radius: 10%;
  position: relative;
  padding-left: 0  !important;
  margin-top: 4%;
}
.shadow{
  background:rgb(28,134,122)
}
.shadow:hover{
  background:rgb(24, 114, 106)
}





</style>