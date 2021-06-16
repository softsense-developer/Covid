<template>
  <div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
        <div class="row d-flex justify-content-center">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid black;">
             <h5 style="color:#607D8B;text-transform: capitalize;">Hastane Bilgileri</h5>
          </div>
        </div>
        <div class="card  mt-5">
        <div class="alert alert-success" role="alert" v-if="flashHospitalAdd">
                   Hastane Başarı bir şekilde Eklendi.
                    </div>
           <div class="card-body">
                <form  @submit.prevent="HospitalAdd()">
                    <div class="form-group">
                        <label for="hospitalName">Hastane İsmi</label>
                        <input type="text" v-model="hospitalName" id="hospitalName" name="hospitalName" class="form-control" :class="{ 'is-invalid': submitted && $v.hospitalName.$error }" />
                        <div v-if="submitted && $v.hospitalName.$error" class="invalid-feedback">
                        <span v-if="!$v.hospitalName.required">Hastane ismi boş olamaz</span>
                       </div>
                      </div>
                     <div class="form-group">
                        <label for="hospitalName">Adres</label>
                        <input type="text" v-model="address" id="address" name="address" class="form-control" :class="{ 'is-invalid': submitted && $v.address.$error }" />
                        <div v-if="submitted && $v.address.$error" class="invalid-feedback">
                        <span v-if="!$v.address.required">Adres Boş Olamaz</span>
                       </div>
                      </div>
                        <div class="form-group">
                        <label for="hospitalName">Hastane Kapasitesi</label>
                        <input type="text" v-model="hospitalCapacity" id="hospitalCapacity" name="hospitalCapacity" class="form-control" :class="{ 'is-invalid': submitted && $v.hospitalCapacity.$error }" />
                        <div v-if="submitted && $v.hospitalCapacity.$error" class="invalid-feedback">
                        <span v-if="!$v.hospitalCapacity.required">Hastane Kapasitesi Boş Olamaz</span>
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
import { required } from "vuelidate/lib/validators";

export default {
  name:'MainHospitalAdd',
  data() {
    return { 
      hospitalName:"",
      address: "",
      hospitalCapacity: "",
      flashHospitalAdd:false,
      submitted: false,
    }
  },
  validations:{
  
    hospitalName:{required},
    address: { required},
    hospitalCapacity: { required }
  
},
  methods: {
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
    console.log(res);
      this.flashHospitalAdd=true;
      setTimeout(() => this.flashHospitalAdd = false, 3000);
      
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