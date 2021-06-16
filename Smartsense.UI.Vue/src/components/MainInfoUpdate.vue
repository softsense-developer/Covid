<template>
  <div>
    <div id="snackbar">Some text some message..</div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container" >
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Kişisel Bilgiler</h5>
          </div>
        </div>
        <div class="row d-flex justify-content-start rows" >
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5" >
            <div class="account-update" >
              <div class="form-container">
                <div class="form-inner">
                  <form  class="login" @submit.prevent="Update()">
                    <div class="form-group ">
                      <label>İsminiz</label>
                      <input type="text" v-model="name" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.name.$error }" />
                      <div v-if="submitted && $v.name.$error" class="invalid-feedback">
                        <span v-if="!$v.name.required">İsim Boş Olamaz</span>
                        <span v-if="!$v.name.minLength">İsim minumum 2 maksimum 40 karakter olabilir</span>
                        <span v-if="!$v.name.maxLength">İsim  maksimum 40 karakter olabilir</span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label>Soy isminiz</label>
                      <input type="text" v-model="surname" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.surname.$error }" />
                      <div v-if="submitted && $v.surname.$error" class="invalid-feedback">
                        <span v-if="!$v.name.required">Soyisim  Boş Olamaz</span>
                        <span v-if="!$v.surname.minLength">Soyisim minimum 2 maksimum 40 karakter olmalıdır</span>
                        <span v-if="!$v.surname.maxLength">Soyisim  maksimum 40 karakter olabilir</span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label>Telefon Numaranız</label>
                      <input type="text" v-model="phone" id="oldpassword" name="password" class="form-control" :class="{ 'is-invalid': submitted && $v.phone.$error }" />
                        <div v-if="submitted && $v.phone.$error" class="invalid-feedback">
                          <span v-if="!$v.phone.required">Telefon Numarası Boş Olamaz</span>
                          <span v-if="!$v.phone.minLength">Telefon Numarası minimum 10 maksimum 15 karakter olmalı</span>
                        <span v-if="!$v.phone.maxLength">Telefon Numarası maksimum 15 karakter olmalı</span>
                        <span v-if="!$v.phone.numeric"><br>Lütfen Sayısal Karakter giriniz</span>
                        </div>
                    </div>
                    
                    <!-- <input value="Bilgilerimi Güncelle" type="button" @click="Update()"   > -->
                    <div class="form-group mt-5 d-flex justify-content-center">
                        <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Bilgilerimi Güncelle</button>
                    </div>
                    <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;"></div>
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
import { required,minLength,maxLength,numeric } from "vuelidate/lib/validators";
export default {
name:"infoUpdate",
data(){
  return{
  name:"",
  surname:"",
  phone:"",
  flashmessage:false,
   submitted: false
  }
},
mounted() {
   this.getProfiles()
  
},
validations:{
  name:{required,
  minLength: minLength(2),
  maxLength: maxLength(40)
  },
  surname: { required,
  minLength: minLength(2),
  maxLength: maxLength(40)},

  phone: { required,numeric,
  minLength: minLength(10),
  maxLength: maxLength(15)},
   
  
},

methods:{
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

  getProfiles() {
    var url='http://api.smartsense.com.tr/api/auth/getinfo'
    var token=localStorage.getItem('token');
    axios.get(url,{
        headers:{'Authorization': `Bearer ${token}`}
    }).then(res=> {
        console.log(res);
        if(res.data.code=="200") {
          console.log(res);
          console.log("merhabaaa");
          this.name=res.data.name;
          this.surname=res.data.surname;
          this.phone=res.data.phone;
        } else {
          // this.pageMode=1;
          }
      })
  },
  Update() {
    this.submitted = true;
    // stop here if form is invalid
    this.$v.$touch();
    if (this.$v.$invalid) {
      return;
    }
    var url='http://api.smartsense.com.tr/api/auth/putinfo'
    var token=localStorage.getItem('token');
    var JsonObjectsx= {
      "name": this.name,
      "surname": this.surname,
      "phone": this.phone,
    }
    console.log(JsonObjectsx);
    axios.post(url,JsonObjectsx, {
     headers: {'Authorization': `Bearer ${token}`}
    }).then(res=> {
        console.log(res);
        if(res.data.code==200){
          this.Toast(res.data.message)
        }
    })
  },
  
  
  
}

}

</script>

<style>

</style>