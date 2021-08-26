<template>
  <div>
    <Navbar id="sidebar"/>
    <h1 class="subheading grey--text">Doktor İşlemleri</h1>
    <div class="container">
      <div class="row" v-if="pageMode==1">
      <div class="col-sm-12 col-lg-12 col-md-12 d-flex justify-content-end">
        <i class="fas fa-plus" style="font-size:15px;cursor:pointer;color:green;" data-toggle="modal" data-target="#exampleModalUpdateDoctor"> Doktorunuzu güncellemek için tıklayınız</i>
        <div class="modal fade" id="exampleModalUpdateDoctor" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <span class="headline">Doktor bilgilerini güncelle</span>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                  <div class="card-body mt-5">
                    <div class="card-text">
                      <label for="exampleFormControlInput1">Lütfen hastane seçiniz</label>
                      <select class="form-control" id="exampleFormControlSelect1" @change="getDoctors()" v-model="hospitalId">
                  <option value="" >Lütfen Hastane Seçiniz</option> 
                  <option v-for="o of hospitals" :key="o.id" v-bind:value="o.id">{{o.hospitalName}}</option>
                </select>
                <label for="exampleFormControlInput1" class="mt-2">Doktor seçmek için tıklayınız</label>
                <select class="form-control" id="exampleFormControlSelect1" v-model="doctorId">
                  <option value="" >Lütfen Doktor Seçiniz</option> 
                  <option  v-for="o of doctors" :key="o.doctorId"   v-bind:value="o.doctorId">{{o.doctorName}} {{o.doctorSurname}}</option>
                </select>
                      <div class="d-flex justify-content-end mt-3">
                        <button class="btn btn-success w-100" @click="postDoctor()">Doktor Güncelle</button>
                      </div>
                    </div>
  
</div>
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
    <div class="row" v-if="pageMode==1">
      <div class="col-sm-12 col-md-12 col-lg-12">
        <div class="card d-flex justify-content-center" style="align-items:center;">
          <div class="d-flex justify-content-center" style="align-items:center">
            <h5>Seçili Doktorunuz-</h5>
           <h3 class="badge badge-success mt-1 ml-2"> {{profile[0].data.doctorName}}</h3>
          </div>
          
        </div>
      </div>
    </div>
        <div class="row" v-if="pageMode==0">
          <div class="card" style="width: 100%;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);">
            <div class="container" >
              <div class="row" >
                <div class="col-sm-12 col-md-6 col-lg-6" >
                  <img class="card-img-top" src="../assets/doctor.jpg" alt="Card image cap" style="width:95%;height:350px;border-radius:15px">
                </div>
                <div class="col-sm-12 col-md-6 col-lg-6">
                  <div class="card-body mt-5">
                    <div class="card-text">
                      <small class="form-text text-muted">Hastane seçmek için tıklayınız</small>
                      <select class="form-control" id="exampleFormControlSelect1" @change="getDoctors()" v-model="hospitalId">
                  <option value="" >Lütfen Hastane Seçiniz</option> 
                  <option v-for="o of hospitals" :key="o.id" v-bind:value="o.id">{{o.hospitalName}}</option>
                </select>
                <small class="form-text text-muted mt-4">Doktor seçmek için tıklayınız</small>
                <select class="form-control" id="exampleFormControlSelect1" v-model="doctorId">
                  <option value="" >Lütfen Doktor Seçiniz</option> 
                  <option  v-for="o of doctors" :key="o.doctorId"   v-bind:value="o.doctorId">{{o.doctorName}} {{o.doctorSurname}}</option>
                </select>
                      <div class="d-flex justify-content-end mt-3">
                        <button class="btn btn-success" @click="postDoctor()">Doktor seç</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
  
</div>
          <!-- doctor seçilmemişse bu colon çalışacak -->
          <!-- eğer doctor varsa bu colon çalışacak -->
          <!-- <div class="col-sm-12 col-md-12 col-lg-12" v-show="pageMode==1" v-for="(doctor,index) in doctorNameSurName " :key="index">
            <div class="card-requiest mt-3" >
                  <div class="card-body d-flex justify-content-between" >
                    <p class="card-title ">Varsayılan doktor- {{doctor.doctorNameSurName}}</p>
                    <div class="card-title ">
                      <h5><button class="badge badge-danger mr-1"  data-toggle="modal" data-target="#exampleModalAccDel" >Doktor sil</button></h5>
                      <div class="modal fade" id="exampleModalAccDel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title" id="exampleModalLabel">Insense</h5>
                              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                            <div class="modal-body">
                              Bu doktoru silmek istediğinize eminmisiniz?
                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-success" data-dismiss="modal">Hayır</button>
                              <button type="button" class="btn btn-outline-danger" @click="doctorDelete()" data-dismiss="modal">Evet</button>
                            </div>
                          </div>
                        </div>
                      </div>             
                    </div>
                 </div>
            </div>
          </div> -->
        </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import Navbar from '@/components/Navbar'
import Vue from 'vue'
// import Vue from 'vue'
export default {
  components :{
    Navbar,
  },
  mounted () {
    this.getHospitals();
    // this.getDoctors();
    this.getProfile();
  },
  data () {
    return {
      ax:'',
     hospitals:[],
     hospitalId:'',
     doctors:[],
     doctorId:'',
     doctorName:"",
     hospitalName:"",
    //  opacity:0,
     display:'block',
     pageMode:null,
     profile:[]
    }
  },
  methods :{
    getDoctors() {
    this.doctors.length="";
    var url='http://api.smartsense.com.tr/api/patient/getdoctor'+`/${this.hospitalId}`;
    var token=localStorage.getItem('token');
    axios.get(url, {
     headers:{
       'Authorization': `Bearer ${token}`
     },
    }).then(res=> {
        console.log(res);
        res.data.doctors.forEach(element=> {
         console.log(element);
         this.doctors.push(element);
        })
      })
    console.log(this.doctors);  
    },
    postDoctor() {
      if(this.doctorId !=='') {
        var url='http://api.smartsense.com.tr/api/patient/adddoctor'+`/${this.doctorId}`;
        var token=localStorage.getItem('token');
        axios.get(url, {
          headers: {
            'Authorization': `Bearer ${token}`
          },
        }).then(res=> {
            console.log(res);
            if (res.data.code == 200) {
              Vue.$toast.success('doktora istek gönderildi')
            }
            else if (res.data.code == 400) {
                res.data.errors.forEach(el=> {
                  Vue.$toast.error(el)
                })
              
            }
          }).catch(err=> {
              Vue.$toast.error(err);
          })
      }
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
          // this.doctor=true
          this.pageMode = 1
        } else {
          // this.doctor=false
          this.pageMode=0
        }
        
      }).catch(err=> {
        console.log(err);
        // Vue.$toast.error(err);
      })
    },
    getHospitals(){
     var url='http://api.smartsense.com.tr/api/patient/gethospital';
     var token=localStorage.getItem('token');
     axios.get(url,{
      headers:{
         'Authorization': `Bearer ${token}`
        },
      }).then(res=> {
         console.log(res);
         res.data.hospitals.forEach(element => {
          console.log(element);
          this.hospitals.push(element);
         });
        console.log(this.hospitals);
        })
      },
    
  }
}
</script>

<style scoped>

 .card-requiest {
    border-top: 1px solid green;
    display: flex;
    width: 100%;
    height: 75%;
    background: rgba(255, 255, 255, 0.375);
    box-shadow: 0 0.75rem 2rem 0 rgb(0 0 0 / 10%);
    border-radius: 2rem;

  }
  @media screen and (max-width: 800px) {
    img {
      display: none;
    }
  }
  

</style>