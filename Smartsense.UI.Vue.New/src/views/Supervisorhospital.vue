<template>
<div>
  <Navbar id="sidebar"/>
  <h1 class="subheading grey--text">Hastane</h1>
  <div class="container">
    <div class="row">
      <div class="col-sm-12 col-lg-12 col-md-12 d-flex justify-content-end">
        <i class="fas fa-plus" style="font-size:15px;cursor:pointer;color:green;" data-toggle="modal" data-target="#exampleModal"> Hastane bilgilerini düzenlemek için tıklayın</i>
        <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <span class="headline">Hastane bilgilerini düzenle</span>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
              <form @submit.prevent="Update()">
                <div class="form-group">
                  <label for="password">hastane ismi giriniz</label>
                  <input type="text" v-model="objects.hospitalName" id="hospitalName" name="hospitalName" class="form-control" :class="{ 'is-invalid': submitted && $v.objects.hospitalName.$error }" />
                  <div v-if="submitted && $v.objects.hospitalName.$error" class="invalid-feedback">
                    <span v-if="!$v.objects.hospitalName.required">hastane ismi  Boş Olamaz</span>
                   <!-- <span v-if="!$v.objects.hospitalName.minLength">Hastane İsmi  minumum 6 maksimum 70 karakter olabilir</span>
                    <span v-if="!$v.objects.hospitalName.maxLength">Hastane İsmi  maksimum 70 karakter olabilir</span>-->
                  </div>
                </div>
                <div class="form-group">
                  <label for="password">hastane adresi giriniz</label>
                  <input type="text" v-model="objects.address" id="address" name="address" class="form-control" :class="{ 'is-invalid': submitted && $v.objects.address.$error }" />
                  <div v-if="submitted && $v.objects.address.$error" class="invalid-feedback">
                    <span v-if="!$v.objects.address.required">hastane adresi Boş Olamaz</span>
                    <span v-if="!$v.objects.address.minLength">Adres  minumum 2 maksimum 119 karakter olabilir</span>
                        <span v-if="!$v.objects.address.maxLength">Adres maksimum 119 karakter olabilir</span>
                  </div>
                </div>
                <div class="form-group">
                  <label for="password">hastane kapasitesi giriniz</label>
                  <input type="text" v-model="objects.capacity" id="capacity" name="capacity" class="form-control" :class="{ 'is-invalid': submitted && $v.objects.capacity.$error }" />
                  <div v-if="submitted && $v.objects.capacity.$error" class="invalid-feedback">
                    <span v-if="!$v.objects.capacity.required">hastane kapasitesi  Boş Olamaz</span>
                     <span v-if="!$v.objects.capacity.numeric"><br>Lütfen Sayısal Karakter giriniz</span>
                        <span v-if="!$v.objects.capacity.between"><br>Kapasitye minumum 2 maksimum 50.000 olabilir</span>
                </div>
                </div>
                <div class="form-group mt-3 mb-3 d-flex justify-content-center">
                  <button class="btn btn-success w-100">Güncelle</button>
                </div>
            </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-outline-danger" data-dismiss="modal">Close</button>
                <!-- <button type="button" class="btn btn-success" @click="gönder()">İlaç Ekle</button> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
        <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="card">
            <div class="container">
              <div class="row" >
                <div class="col-sm-6 col-md-6 col-lg-6 d-flex justify-content-start" >
                  <img src="../assets/hospital.jpg" id="person-img" alt="" />
                </div>
                <div class="col-sm-6 col-md-6 col-lg-6 d-flex flex-column justify-content-center">
                  <h6 ><span style="color:rgb(28,134,122)">hastane:</span> {{hospitalName}}</h6>
                  <h6><span  style="color:rgb(28,134,122)">Adres:</span> {{address}}</h6>
                  <h6><span  style="color:rgb(28,134,122)">hastane kapasite:</span> {{capacity}}</h6>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
    
  </div>
  </div>
</template>

<script>
// Main JS (in UMD format)
// import VueTimepicker from 'vue2-timepicker'
import Vue from 'vue';
// import VueToast from 'vue-toast-notification';
import { required ,minLength,maxLength ,numeric,between  } from "vuelidate/lib/validators";
import Navbar from '@/components/Navbarsupervisor'
// CSS
// import 'vue2-timepicker/dist/VueTimepicker.css'
import axios from 'axios'
export default {
  components :{
    // VueTimepicker,
    Navbar,
  },
  data(){
    return {
      pageMode:0,
      objects: {
        hospitalName:'',
        address:'',
        capacity:'',
      },
      hospital:[],
      errors:[],
      submitted:false,
      hospitalName:'',
        address:'',
        capacity:'',
    }
  },
  validations:{
      objects: {
        hospitalName:{required,
         minLength: minLength(6),
         maxLength: maxLength(70)
         },
        address: { required,
        minLength: minLength(2),
        maxLength: maxLength(119)
        },
        capacity: { required,
        numeric,
        between: between(2,50000)
        }
      }
    },
    mounted() {
     this.getHospital()
    },
  methods: {
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
          if(res.data.code == "200") {
            Vue.$toast.success("hastane bilgileri başarıyla düzenlendi")   
          }
        })
      },
      getHospital(){
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
    SeizureDisplay(id) {
      var title=document.querySelectorAll('.question');
      title.forEach(element=> {
        if(element.id==id) {
          element.classList.toggle('show-text');
        }
      })
    },
  }
}
</script>

<style scoped>

.card {
    overflow: hidden;
    width: 100%;
    background: #fff;
    padding: 30px;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);
  }


ul {
  list-style-type: none;
}
a {
  text-decoration: none;
}

h2,
h3,
h4 {
  letter-spacing: var(--spacing);
  text-transform: capitalize;
  line-height: 1.25;
  margin-bottom: 0.75rem;
  font-family: var(--ff-primary);
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



