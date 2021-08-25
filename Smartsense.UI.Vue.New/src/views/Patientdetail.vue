<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">Hasta Detayı</h1>
    <div class="container">
      <div class="row" v-if="pageMode==1">
      <div class="col-sm-12 col-lg-12 col-md-12 d-flex justify-content-end">
        <i class="fas fa-plus" style="font-size:15px;cursor:pointer;color:green;" data-toggle="modal" data-target="#exampleModal"> Kişisel bilgileri düzenlemek için tıklayın</i>
        <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <span class="headline">Kişisel bilgileri düzenle</span>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
               <form @submit.prevent="onUpdate()">
  <div class="form-group">
    <label for="exampleFormControlInput1">Kimlik numaranızı giriniz</label>
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
  <div class="form-group">
    <label for="exampleFormControlSelect1">Kan grubu seçiniz</label>
    <select  id="exampleFormControlSelect1"  @input=$v.blood.$touch()
                v-model="blood"
                type="tel"
                :class="{ 'is-invalid': submitted && $v.gender.$error }"
                class="form-control" 
                aria-describedby="emailHelp">
      <option disabled value="">Lütfen kan grubu seçiniz</option> 
      <option>0Rh+</option>
      <option>0Rh-</option>
      <option>ABRh+</option>
      <option>ABRh-</option>
      <option>ARh+</option>
      <option>ARh-</option>
      <option>BRh+</option>
      <option>BRh-</option>
    </select>
    <div v-if="submitted && $v.blood.$error" class="invalid-feedback">
                  <div v-if="!$v.blood.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  <div class="form-group">
    <label for="exampleFormControlSelect1">Cinsiyet seçiniz</label>
    <select  id="exampleFormControlSelectone"  @input=$v.gender.$touch()
                v-model="gender"
                type="tel"
                :class="{ 'is-invalid': submitted && $v.gender.$error }"
                class="form-control" 
                aria-describedby="emailHelp">
      <option disabled value="null">Lütfen cinsiyet seçiniz</option> 
      <option value="0">Erkek</option>
      <option value="1">Kadın</option>
      <option value="2">Belirtmek istemiyorum</option>
    </select>
    <div v-if="submitted && $v.gender.$error" class="invalid-feedback">
                  <div v-if="!$v.gender.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  <div class="form-group">
    <label for="exampleFormControlSelect1">Durum seçiniz</label>
    <select  id="exampleFormControlSelecttwo"  @input=$v.state.$touch()
                v-model="state"
                type="text"
                :class="{ 'is-invalid': submitted && $v.state.$error }"
                class="form-control" 
                aria-describedby="emailHelp">
      <option disabled value="null">Lütfen cinsiyet seçiniz</option> 
      <option value="0">Hastanede Karantinada</option>
      <option value="1">Evde karantinada</option>
      <option value="2">Şüpheli</option>
      <option value="3">Tahliye</option>
      <option value="4">Diğer</option>
    </select>
    <div v-if="submitted && $v.state.$error" class="invalid-feedback">
                  <div v-if="!$v.state.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  <div class="form-group">
    <label for="exampleFormControlSelect2">Doğum tarihiniz seçiniz</label>
    <input type="date" class="form-control" id="birthday" name="birthday" v-model="bdate" required>
               
  </div>
  <div class="form-group">
    <div class="form-group">
    <label for="exampleFormControlInput1">Teşhis giriniz</label>
    <input 
                v-on:keyup.enter="onSubmit()"
                @input=$v.teshis.$touch()
                v-model="teshis"
                type="text" 
                :class="{ 'is-invalid': submitted && $v.teshis.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.teshis.$error" class="invalid-feedback">
                  <div v-if="!$v.teshis.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  </div>
  
  
  <button class="btn btn-success w-100">Bilgileri kaydet</button>
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
      <div class="row" v-if="pageMode==0">
        <div class="col-sm-12 col-md-12 col-lg-12">
          <div class="card">
            <form @submit.prevent="onSubmit()">
  <div class="form-group">
    <label for="exampleFormControlInput1">Kimlik numaranızı giriniz</label>
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
  <div class="form-group">
    <label for="exampleFormControlSelect1">Kan grubu seçiniz</label>
    <select  id="exampleFormControlSelect1"  @input=$v.blood.$touch()
                v-model="blood"
                type="tel"
                :class="{ 'is-invalid': submitted && $v.gender.$error }"
                class="form-control" 
                aria-describedby="emailHelp">
      <option disabled value="">Lütfen kan grubu seçiniz</option> 
      <option>0Rh+</option>
      <option>0Rh-</option>
      <option>ABRh+</option>
      <option>ABRh-</option>
      <option>ARh+</option>
      <option>ARh-</option>
      <option>BRh+</option>
      <option>BRh-</option>
    </select>
    <div v-if="submitted && $v.blood.$error" class="invalid-feedback">
                  <div v-if="!$v.blood.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  <div class="form-group">
    <label for="exampleFormControlSelect1">Cinsiyet seçiniz</label>
    <select  id="exampleFormControlSelectone"  @input=$v.gender.$touch()
                v-model="gender"
                type="tel"
                :class="{ 'is-invalid': submitted && $v.gender.$error }"
                class="form-control" 
                aria-describedby="emailHelp">
      <option disabled value="null">Lütfen cinsiyet seçiniz</option> 
      <option value="0">Erkek</option>
      <option value="1">Kadın</option>
      <option value="2">Belirtmek istemiyorum</option>
    </select>
    <div v-if="submitted && $v.gender.$error" class="invalid-feedback">
                  <div v-if="!$v.gender.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  <div class="form-group">
    <label for="exampleFormControlSelect1">Durum seçiniz</label>
    <select  id="exampleFormControlSelecttwo"  @input=$v.state.$touch()
                v-model="state"
                type="text"
                :class="{ 'is-invalid': submitted && $v.state.$error }"
                class="form-control" 
                aria-describedby="emailHelp">
      <option disabled value="null">Lütfen cinsiyet seçiniz</option> 
      <option value="0">Hastanede Karantinada</option>
      <option value="1">Evde karantinada</option>
      <option value="2">Şüpheli</option>
      <option value="3">Tahliye</option>
      <option value="4">Diğer</option>
    </select>
    <div v-if="submitted && $v.state.$error" class="invalid-feedback">
                  <div v-if="!$v.state.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  <div class="form-group">
    <label for="exampleFormControlSelect2">Doğum tarihiniz seçiniz</label>
    <input type="date" class="form-control" id="birthday" name="birthday" v-model="bdate" required>
               
  </div>
  <div class="form-group">
    <div class="form-group">
    <label for="exampleFormControlInput1">Teşhis giriniz</label>
    <input 
                v-on:keyup.enter="onSubmit()"
                @input=$v.teshis.$touch()
                v-model="teshis"
                type="text" 
                :class="{ 'is-invalid': submitted && $v.teshis.$error }"
                class="form-control" 
                id="exampleInputEmail1" 
                aria-describedby="emailHelp">
                <div v-if="submitted && $v.teshis.$error" class="invalid-feedback">
                  <div v-if="!$v.teshis.required"  id="emailHelp" class="form-text">bu alan doldurulmalıdır</div>
                </div>
  </div>
  </div>
  
  
  <button class="btn btn-success w-100">Bilgileri kaydet</button>
</form>
          </div>
        </div>
      </div>
      <div class="row" v-if="pageMode==1">
        <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="row" v-for="profile in profile" :key="profile.data.doctorId">
              <div class="card" style="width: 100%;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);">
            <div class="container" >
              <div class="row" >
                <div class="col-sm-12 col-md-6 col-lg-6" >
                  <img class="card-img-top" src="../assets/hospital.jpg" alt="Card image cap" style="width:95%;height:350px;border-radius:15px">
                </div>
                <div class="col-sm-12 col-md-6 col-lg-6">
                  <p  v-if="doctor==true"><span class="text-success" >Doktorunuz - </span> {{profile.data.doctorName}}</p>
                  <p v-if="doctor==false"><span class="text-success" >Doktorunuz - </span> Doktor seçimi yapmalısınız !!</p>
                  <p><span class="text-success" >Mail adresiniz - </span> {{profile.data.email}}</p>
                  <p><span class="text-success" >Doğum tarihiniz - </span> {{profile.data.dateOfBirth.slice(0,10)}}</p>
                  <p><span class="text-success" >Teşhis - </span> {{profile.data.diagnosis}}</p>
                  <p style="border-bottom:1px solid black"></p>
                  <p v-if="profile.data.userStatus == 0"><span class="text-success" >Durum -</span> Hastanede karantinada</p>
                  <p v-if="profile.data.userStatus == 1"><span class="text-success" >Durum -</span> Evde karantinada</p>
                  <p v-if="profile.data.userStatus == 2"><span class="text-success" >Durum -</span> Şüpheli</p>
                  <p v-if="profile.data.userStatus == 3"><span class="text-success" >Durum -</span> Taburcu</p>
                  <p v-if="profile.data.userStatus == 4"><span class="text-success" >Durum -</span> Diğer</p>
                  <p><span class="text-success" >Kan grubunuz - </span> {{profile.data.bloodGroup}}</p>
                  <p><span class="text-success" >Kimlik numaranız - </span> {{profile.data.identityNumber}}</p>
                  <p v-if="doctor==false"><span class="text-success" >Hastane ismi - </span> Hastane seçimi yapmalısınız !!</p>
                </div>
              </div>
            </div>
  
</div>
            </div>
            <!-- <div class="card-body">
              <h4 class="card-title text-center">Hasta Adı - {{profile.data.name}} {{profile.data.surname}}</h4>
              <h6 class="card-subtitle mb-2 text-center" style="border-bottom:1px solid green"></h6>
              <div class="row">
                <div class="col-sm-12 col-md-6 col-lg-6 mt-3">
                  <p  v-if="doctor==true"><span class="text-success" >Doktorunuz-</span> {{profile.data.doctorName}}</p>
                  <p v-if="doctor==false"><span class="text-success" >Doktorunuz-</span> Doktor seçimi yapmalısınız!</p>
                  <p><span class="text-success" >Mail adresiniz-</span> {{profile.data.email}}</p>
                  <p><span class="text-success" >Doğum tarihiniz-</span> {{profile.data.dateOfBirth.slice(0,10)}}</p>
                  <p><span class="text-success" >Teşhis-</span> {{profile.data.diagnosis}}</p>
                </div>
                <div class="col-sm-12 col-md-6 col-lg-6 mt-2">
                  <p v-if="profile.data.userStatus == 0"><span class="text-success" >Durum-</span> Hastanede karantinada</p>
                  <p v-if="profile.data.userStatus == 1"><span class="text-success" >Durum-</span> Evde karantinada</p>
                  <p v-if="profile.data.userStatus == 2"><span class="text-success" >Durum-</span> Şüpheli</p>
                  <p v-if="profile.data.userStatus == 3"><span class="text-success" >Durum-</span> Taburcu</p>
                  <p v-if="profile.data.userStatus == 4"><span class="text-success" >Durum-</span> Diğer</p>
                  <p><span class="text-success" >Kan grubunuz-</span> {{profile.data.bloodGroup}}</p>
                  <p><span class="text-success" >Kimlik numaranız-</span> {{profile.data.identityNumber}}</p>
                  <p><span class="text-success" >Hastane ismi-</span> {{profile.data.hospitalName}}</p>
                </div>
              </div>
            </div> -->
         
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import Vue from 'vue'
import {required,numeric,minLength,maxLength} from 'vuelidate/lib/validators'
import Navbar from '@/components/Navbar'
// import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
export default {
  name: 'team',
  components: {
   Navbar
  },
  data : () => ({
    state:null,
    bdate:"",
    gender:null,
    password:null,
    teshis:"",
    submitted:false,
    blood:"",
    profile:[],
    pageMode:null,
    doctor:null,
    team: [
      {name: 'Iyad', role: 'web developer', avatar:'/img1.png'},
      {name: 'Reda', role: 'Graphic designer', avatar:'/img2.png'},
      {name: 'Zineb', role: 'web developer', avatar:'/img3.png'},
      {name: 'Hu TechGroup', role: 'Desktop developer', avatar:'/img4.png'},
    ]
  }),
  validations: {
    state: {
      required,
    },
    teshis: {
      required,
    },
    gender: {
       required
     },
     blood: {
       required
     },
    password: {
      required,
      numeric,
      minLength: minLength(11),
      maxLength: maxLength(11)
    }
  },
  mounted () {
    this.getProfile();
  },
  methods: {
    onSubmit() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid){
      return;
    }
      this.gönder();
      
    },
    onUpdate() {
      this.submitted=true;
      this.$v.$touch();
      if (this.$v.$invalid){
      return;
    }
      this.update();
      
    },
    update() {
      var url="http://api.smartsense.com.tr/api/patient/putinfo"
      var token=localStorage.getItem('token');
      var selectkutu = document.getElementById('exampleFormControlSelectone');
      var selectkutu1 = document.getElementById('exampleFormControlSelecttwo');
		var selectkutu_value = selectkutu.options[selectkutu.selectedIndex].value;
    var selectkutu_value1 = selectkutu1.options[selectkutu1.selectedIndex].value;
      var model= {
        "identityNumber": this.password,
        "gender": Number(selectkutu_value),
        "dateOfBirth": this.bdate,
        "userStatus": Number(selectkutu_value1),
        "diagnosis": this.teshis,
        "bloodGroup": this.blood
      }
      console.log(model);
      axios.put(url,model, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        if(res.data.code == "200") {
          Vue.$toast.success("Bilgiler başarıyla güncellenmiştir");
          this.profile=[]
          this.getProfile();
        }
      }).catch(err=> {
        console.log(err);
      })
    },
    gönder() {
      var url="http://api.smartsense.com.tr/api/patient/addinfo"
      var token=localStorage.getItem('token');
      var selectkutu = document.getElementById('exampleFormControlSelectone');
    var selectkutu1 = document.getElementById('exampleFormControlSelecttwo');
		var selectkutu_value = selectkutu.options[selectkutu.selectedIndex].value;
    var selectkutu_value1 = selectkutu1.options[selectkutu1.selectedIndex].value;
      var model= {
        "identityNumber": this.password,
        "gender": Number(selectkutu_value),
        "dateOfBirth": this.bdate,
        "userStatus": Number(selectkutu_value1),
        "diagnosis": this.teshis,
        "bloodGroup": this.blood
      }
      console.log(model);
      axios.post(url,model, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        if(res.data.code == "200") {
          Vue.$toast.success("bilgiler başarıyla kaydedildi");
          this.pageMode=1;
        }
      }).catch(err=> {
        console.log(err);
      })
    },
    getProfile() {
      var url="http://api.smartsense.com.tr/api/patient/getinfo"
      var token=localStorage.getItem("token");
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        if(res.data.code == "200") {
          console.log(res.data.doctorName)
          this.profile.push(res);
        } else {
          this.pageMode = 0;
        }
        console.log(this.profile);
        if(this.profile[0].data.doctorName !== ""){
          this.doctor=true
          this.pageMode = 1
        } else {
          this.doctor=false
          this.pageMode=1
        }
        
      }).catch(err=> {
        Vue.$toast.error(err);
      })
    }
  }
}
</script>
<style scoped>
  .card {
    /* border:1px solid green; */
    overflow: hidden;
    width: 100%;
    background: #fff;
    padding: 30px;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);
  }
  span {
    font-weight:bold;
  }
  @media screen and (max-width: 800px) {
    img {
      display: none;
    }
  }
</style>
