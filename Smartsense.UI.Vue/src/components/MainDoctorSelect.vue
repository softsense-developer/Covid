<template>
  <div>
    <div id="snackbar">Some text some message..</div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Doktor Seçimi</h5>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-sm-12 col-lg-12 col-md-12">
            <div class="card ">
              <div class="card-body">
                <div class="d-flex justify-content-center" v-if="pageMode==0">
                  <footer class="blockquote-footer" style="font-size:1.3rem;">
                  Seçili doktorunuz<cite title="Source Title"> bulunmamaktadır</cite>
                </footer>
                </div>
                <div class="container " v-if="pageMode==1" >
                  <div class="row" >
                    <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center align-items-center ">
                      <i class="fas fa-hospital-symbol d-flex text-info" style="font-size:30px;"> <h5 class="ml-2 mt-2">{{hospitalName}}</h5></i>
                    </div>
                  </div>
                  <div class="row mt-3" >
                    <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center align-items-center ">
                      <i class="fas fa-user-md d-flex text-info" style="font-size:30px;"> <h5 class="ml-2 mt-2">{{doctorName}}</h5></i>
                    </div>
                  </div>
                </div>
                <!-- <div class=" d-flex justify-content-start" style="margin-left:40%">
                  <i class="fas fa-h-square" style="font-size:20px"></i>
                  <p >
                      Seçili Doktor- <span style="text-transform:lowercase">{{this.doctorName}}</span>
                      </p>
                </div>
                <div class=" d-flex justify-content-start">
                  <i class="fas fa-hospital-user" style="font-size:20px"></i>
                  <p >
                      Seçili Doktor- <span style="text-transform:lowercase">{{this.hospitalName}}</span>
                      </p>
                  
                </div> -->
                  <div class="d-flex justify-content-center mt-2">
                    <button class="button btn-one" @click="doctorSelectDisplay()" v-if="pageMode==0"><span>Doktor seç </span></button>
                    <button class="button btn-one mt-5 w-100" v-if="pageMode==1" @click="doctorSelectDisplay()"><span>Güncelle</span></button>
                  </div>
              </div>
              <!--  -->
              <div class="card-footer doctorSelect" style="display:none">
                <select class="form-control" id="exampleFormControlSelect1" @change="getDoctors()" v-model="hospitalId">
                  <option value="" >Lütfen Hastane Seçiniz</option> 
                  <option v-for="o of hospitals" :key="o.id" v-bind:value="o.id">{{o.hospitalName}}</option>
                </select>
                <select class="form-control mt-3" id="exampleFormControlSelect1" v-model="doctorId">
                  <option value="" >Lütfen Hastane Seçiniz</option> 
                  <option  v-for="o of doctors" :key="o.doctorId"   v-bind:value="o.doctorId">{{o.doctorName}} {{o.doctorSurname}}</option>
                </select>
                <div class="d-flex justify-content-end">
                  <button class="button mt-3" @click="postDoctor()"><span>Doktor seç </span></button>
                  <button class="button mt-3" @click="doctorSelectDisplay()" style="background-color:#E57373;"><span>İptal </span></button>
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
export default {
  name:"MainDoctorSelect",
  components:{
  },
  data(){
    return{
     ax:'',
     hospitals:[],
     hospitalId:'',
     doctors:[],
     doctorId:'',
     doctorName:"",
     hospitalName:"",
    //  opacity:0,
     display:'block',
     sayac:1,
     pageMode:0
    }
  },
  mounted() {
    this.getHospitals();
    this.getDoctorName();
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

  ToastError(messages) {
  // Get the snackbar DIV
    var x = document.getElementById("snackbar");
    // Add the "show" class to DIV
    x.className = "show";
    x.innerHTML=`<p style='color:white;'>${messages}</p>`
    x.style.backgroundColor='#EF5350';
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  },

    doctorSelectDisplay() {
      this.sayac++
      var selectbutton=document.querySelector('.doctorSelect');
      var btn_one=document.querySelector('.btn-one');
      if(this.sayac % 2==0) {
        selectbutton.style.display='block';
        btn_one.style.display='none'
      }else {
        selectbutton.style.display='none';
        btn_one.style.display='block'
       } 
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
              this.Toast('doktora istek başarıyla gönderildi');
            }
            else if (res.data.code == 400) {
              this.ToastError(res.data.errors);
            }
          })
      }
     else {
       this.ToastError("Bilgilerinizi kontrol ediniz")
     } 
   },

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
      getDoctorName(){
     var url='http://api.smartsense.com.tr/api/patient/getinfo';
     var token=localStorage.getItem('token');
     axios.get(url,{
      headers:{
         'Authorization': `Bearer ${token}`
        },
      }).then(res=> {
         console.log(res);
         if(res.data.doctorName !== ""){
           this.pageMode=1;
           this.doctorName=res.data.doctorName;
           this.hospitalName=res.data.hospitalName;
         }
          // this.doctorName=res.data.doctorName;
          // this.hospitalName=res.data.hospitalName;
         console.log(this.doctorname);
        })
      }
    }
 }
</script>

<style scoped>
  @import '../assets/css/toast.css';
 .button {
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 4px;
    background-color: rgb(33,140,127);
    border: none;
    color: #FFFFFF;
    /* text-align: center; */
    font-size: 15px;
    /* padding: 10px; */
    width: 200px;
    height: 30px;
    transition: all 0.5s;
    cursor: pointer;
    margin: 5px;
}

 .button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;

}

.button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.button:hover span {
  padding-right: 25px;
}

.button:hover span:after {
  opacity: 1;
  right: 0;
}


</style>

