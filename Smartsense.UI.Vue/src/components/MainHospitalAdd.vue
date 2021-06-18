<template>
  <div>
  <div id="snackbar">Some text some message..</div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
        <div class="row d-flex justify-content-center">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid black;">
             <h5 style="color:#607D8B;text-transform: capitalize;">Hastane Bilgileri</h5>
          </div>
        </div>
        <div class="card  mt-5">
           <div class="card-body">
                <form  @submit.prevent="HospitalAdd()">
                    <div class="form-group">
                        <label for="hospitalName">Hastane İsmi</label>
                        <input type="text" v-model="hospitalName" id="hospitalName" name="hospitalName" class="form-control" :class="{ 'is-invalid': submitted && $v.hospitalName.$error }" />
                        <div v-if="submitted && $v.hospitalName.$error" class="invalid-feedback">
                        <span v-if="!$v.hospitalName.required">Hastane ismi boş olamaz</span>
                        <span v-if="!$v.hospitalName.minLength">Hastane İsmi  minumum 6 maksimum 70 karakter olabilir</span>
                        <span v-if="!$v.hospitalName.maxLength">Hastane İsmi  maksimum 70 karakter olabilir</span>
                       </div>
                      </div>
                     <div class="form-group">
                        <label for="hospitalName">Adres</label>
                        <input type="text" v-model="address" id="address" name="address" class="form-control" :class="{ 'is-invalid': submitted && $v.address.$error }" />
                        <div v-if="submitted && $v.address.$error" class="invalid-feedback">
                        <span v-if="!$v.address.required">Adres Boş Olamaz</span>
                        <span v-if="!$v.address.minLength">Adres  minumum 2 maksimum 119 karakter olabilir</span>
                        <span v-if="!$v.address.maxLength">Adres maksimum 119 karakter olabilir</span>
                       </div>
                      </div>
                        <div class="form-group">
                        <label for="hospitalName">Hastane Kapasitesi</label>
                        <input type="text" v-model="hospitalCapacity" id="hospitalCapacity" name="hospitalCapacity" class="form-control" :class="{ 'is-invalid': submitted && $v.hospitalCapacity.$error }" />
                        <div v-if="submitted && $v.hospitalCapacity.$error" class="invalid-feedback">
                        <span v-if="!$v.hospitalCapacity.required">Hastane Kapasitesi Boş Olamaz</span>
                        <span v-if="!$v.hospitalCapacity.numeric"><br>Lütfen Sayısal Karakter giriniz</span>
                        <span v-if="!$v.hospitalCapacity.between"><br>Kapasitye minumum 2 maksimum 50.000 olabilir</span>
                       </div>
                      </div>
                      <div class="form-group mt-5 d-flex justify-content-center">
                            <button class="btn" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Ekle</button>
                     </div>
                </form>
             </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios'
import { required,minLength,maxLength ,numeric,between } from "vuelidate/lib/validators";

export default {
  name:'MainHospitalAdd',
  data() {
    return { 
      hospitalName:"",
      address: "",
      hospitalCapacity: "",
      flashHospitalAdd:false,
      submitted: false,
      errors:[]
    }
  },
  validations:{
  hospitalName:{required,
     minLength: minLength(6),
     maxLength: maxLength(70)
    },
    address: { required,
    minLength: minLength(2),
    maxLength: maxLength(119)
    },
    hospitalCapacity: { 
    required,
    numeric,
    between: between(2,50000)
    }
  
},
  methods: {
    Toast(messages) {
    var x = document.getElementById("snackbar");
    x.className = "show";
    x.style.backgroundColor='rgb(15,120,108)';
    x.innerHTML=`<p style='color:white;'>${messages}</p>`
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  },
  ToastError() {
  // Get the snackbar DIV
    var x = document.getElementById("snackbar");
    // Add the "show" class to DIV
    x.className = "show";
    x.innerHTML=`<p style='color:white;'>${this.errors}</p>`
    x.style.backgroundColor='#EF5350';
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  },
   HospitalAdd(){
    this.submitted = true;
    // stop here if form is invalid
    this.$v.$touch();
    if (this.$v.$invalid){
      return;
    }
    var url='http://api.smartsense.com.tr/api/admin/addhospital'
    var token=localStorage.getItem('token');
    var user={
    "hospitalName":this.hospitalName,
    "address": this.address,
    "hospitalCapacity": this.hospitalCapacity
    }
    console.log(user);
    axios.post(url,user,{
      headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
      if(res.data.code=='200'){
        this.Toast(res.data.message);
      }
      else if(res.data.errors.length!==null){
        res.data.errors.forEach(el => {
          this.errors.push(el);
        });
        this.ToastError();
      }
    console.log(res);
    
      
      
    })
    
    
    console.log("hastane ekleme");
    }
  }

  
 
}
</script>

<style>

.card {
  border-radius: 10px;
  border: none;
  box-shadow: 0 15px 15px rgba(0, 0, 0, 0.1);
}

</style>