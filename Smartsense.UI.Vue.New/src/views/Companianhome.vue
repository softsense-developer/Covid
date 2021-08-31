<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">Anasayfa</h1>
    <div class="container">
      <div class="row">
        <div class="col-sm-12 col-md-12 col-lg-12"  v-for="(patientsdata,index) in patientsData" :key="index">
          <div class="card card-head" style="margin-bottom:0 !important;border: 1px solid rgb(149, 224, 153); ">
            <div class="d-flex justify-content-end">
              <!-- <i class="fas fa-info-circle text-white m-2" style="font-size:25px;cursor:pointer;" @click="goTodetail(patientsdata.id)"></i> -->
                 
            </div> 
          </div>
          <div class="question" :id="patientsdata.id">   
        <!-- question title -->
          <div class="question-title" >
            <div class="container">
              <div class="row"></div>
              <div class="row">
                <div class="col-sm-6 col-md-4 col-lg-3 d-flex justify-content-start">{{patientsdata.name}} {{patientsdata.surname}}</div>
                <div class="col-sm-6 col-md-4 col-lg-5 d-flex justify-content-between">
                  <div class="mr-5">
                    <span  v-if="patientsdata.pulseWarning==1" ><i class="fas fa-heart" style="color:#EF9A9A;font-size:15px"></i> {{patientsdata.pulse}}</span>
                    <span class="blink" v-if="(patientsdata.pulseWarning==0)||(patientsdata.pulseWarning==2)" ><i class="fas fa-heart" style="color:#E57373"></i> {{patientsdata.pulse}}</span>
                    <span  v-if="(patientsdata.pulseWarning==0)" ><i class="fas fa-arrow-down" style="color:red;font-size:13px"></i></span>
                    <span  v-if="(patientsdata.pulseWarning==2)" ><i class="fas fa-arrow-up" style="color:red;font-size:13px"></i></span>
                  </div>
                  <div class="mr-5">
                    <span  v-if="patientsdata.temperatureWarning==1" ><i class="fas fa-fire" style="color:#EF9A9A;font-size:15px"></i> {{patientsdata.temperature.toFixed(2)}}</span>
                    <span class="blink" v-if="(patientsdata.temperatureWarning==0)||(patientsdata.temperatureWarning==2)" ><i class="fas fa-fire" style="color:#E57373"></i> {{patientsdata.temperature}}</span>
                    <span  v-if="(patientsdata.temperatureWarning==0)" ><i class="fas fa-arrow-down" style="color:red;font-size:13px"></i></span>
                    <span  v-if="(patientsdata.temperatureWarning==2)" ><i class="fas fa-arrow-up" style="color:red;font-size:13px"></i></span>
                  </div>
                  <div class="mr-5">
                    <span  v-if="patientsdata.oxygenWarning==1" ><i class="fas fa-tint" style="color:#90CAF9;font-size:15px"></i> {{patientsdata.oxygen}}</span>
                    <span  v-if="(patientsdata.oxygenWarning==0)||(patientsdata.oxygenWarning==2)" ><i class="fas fa-tint blink" style="color:#64B5F6"></i> {{patientsdata.oxygen}}</span>
                    <span  v-if="(patientsdata.oxygenWarning==0)" ><i class="fas fa-arrow-down" style="color:red;font-size:13px"></i></span>
                    <span  v-if="(patientsdata.oxygenWarning==2)" ><i class="fas fa-arrow-up" style="color:red;font-size:13px"></i></span>
                  </div>
                  <div class="mr-5">
                    <span  v-if="patientsdata.pulseWarning==1" ><i class="fas fa-location-arrow" style="color:#81C784;font-size:15px"></i> </span>
                    <span class="blink" v-if="(patientsdata.pulseWarning==0)||(patientsdata.pulseWarning==2)" ><i class="fas fa-location-arrow" style="color:#64B5F6"></i></span>
                  </div>
                </div>
                <div class="col-lg-4 d-flex justify-content-center">
                  <span class="btn text-light" style="pointer-events:none;background:#E57373;border:none;font-size:12px">{{patientsdata.diagnosis}}</span>
                  <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patientsdata.patientStatus==0">hastanede karantinada</span>
                  <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patientsdata.patientStatus==1">evde karantinada</span>
                  <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patientsdata.patientStatus==2">şüpheli</span>
                  <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patientsdata.patientStatus==3">taburcu</span>
                  <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patientsdata.patientStatus==4">diğer nedenler</span>
                </div>
                <!-- <div class="col-sm-12 col-md-12 col-lg-2 d-flex">
                  {{patientsdata.name}} {{patientsdata.surname}}
                </div>
                <div class="col-sm-12 col-md-12 col-lg-2 d-flex">
                <span  v-if="patientsdata.pulseWarning==1" ><i class="fas fa-heart" style="color:#EF9A9A;font-size:15px"></i> {{patientsdata.pulse}}</span>
                <span class="blink" v-if="(patientsdata.pulseWarning==0)||(patientsdata.pulseWarning==2)" ><i class="fas fa-heart" style="color:#E57373"></i> {{patientsdata.pulse}}</span>
                </div>
                <div class="col-sm-12 col-md-12 col-lg-2 d-flex">
                <span  v-if="patientsdata.temperatureWarning==1" ><i class="fas fa-fire" style="color:#EF9A9A;font-size:15px"></i> {{patientsdata.temperature.toFixed(2)}}</span>
                <span class="blink" v-if="(patientsdata.temperatureWarning==0)||(patientsdata.temperatureWarning==2)" ><i class="fas fa-fire" style="color:#E57373"></i> {{patientsdata.temperature}}</span>
                </div>
                <div class="col-sm-12 col-md-12 col-lg-2 d-flex">
                <span  v-if="patientsdata.oxygenWarning==1" ><i class="fas fa-tint" style="color:#90CAF9;font-size:15px"></i> {{patientsdata.oxygen}}</span>
                <span class="blink" v-if="(patientsdata.oxygenWarning==0)||(patientsdata.oxygenWarning==2)" ><i class="fas fa-tint" style="color:#64B5F6"></i> {{patientsdata.oxygen}}</span>
                </div>
                <div class="col-sm-12 col-md-12 col-lg-2 d-flex">
                <span  v-if="patientsdata.pulseWarning==1" ><i class="fas fa-location-arrow" style="color:#81C784;font-size:15px"></i> </span>
                <span class="blink" v-if="(patientsdata.pulseWarning==0)||(patientsdata.pulseWarning==2)" ><i class="fas fa-location-arrow" style="color:#64B5F6"></i></span>
                </div>
                <div class="col-sm-12 col-md-12 col-lg-2 d-flex">
                  <p class="badge badge-success">Detay göster</p>
                </div> -->
              </div>
              <!-- <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-11 d-flex justify-content-end">
                  <span class="btn ml-2 text-white" style="pointer-events:none;background:#E57373;border:none;font-size:12px">{{patientsdata.lastDataMinute}}</span>
                </div>
              </div> -->
            </div>
            
            <!-- <div>
              <button type="button" class="question-btn text-white">
                <span class="plus-icon">
                  <i class="fas fa-plus-square text-success"></i>
                </span>
                <span class="minus-icon">
                  <i class="far fa-minus-square text-success"></i>
                </span>
              </button>
            </div> -->
              
          </div>
          <!-- doctor delete warnings -->
        <!-- question text -->
        </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import axios from 'axios'
import Navbar from '@/components/Navbarcompanian'
export default {
  name:"doctorhome",
  components: {
      Navbar
  },
  data () {
    return {
      patientsData:[]
    }
  },
  mounted () {
      
    this.getPatients();
  },
  methods: {
    goTodetail(prodId) {
      localStorage.setItem("prodId",prodId);
      this.$router.push({name:'detailcompanian',params:{Pid:prodId}})
    },
    SeizureDisplay(id) {
      console.log(id)
      var title=document.querySelectorAll('.question');
      title.forEach(element=> {
        if(element.id==id) {
          element.classList.toggle('show-text');
        }
      })
    },
    getPatients() {
      var token=localStorage.getItem('token');
      var url='http://api.smartsense.com.tr/api/companion/getCompanionpatients'
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        if(res.data.code == "200") {
          
          res.data.patientsData.forEach(element => {
            this.patientsData.push(element);
          });
        }
      }).catch(err=> {
        console.log(err);
        Vue.$toast.error(err);
      })
    },
    deletepatientlocal(index,id) {
       localStorage.setItem('index',index);
       localStorage.setItem('number',id);
    },
    deletestorage() {
      localStorage.removeItem('index');
      localStorage.removeItem('number');
    },
    // deletePatient() {
    //   var index=localStorage.getItem('index');
    //   var id=localStorage.getItem('number');
    //   var token=localStorage.getItem('token');
    //   var url='http://api.smartsense.com.tr/api/doctor/deletepatient'+`/${id}`
    //   axios.get(url, {
    //     headers:{'Authorization': `Bearer ${token}`}
    //   }).then(res=> {
    //     console.log(res);
    //     if(res.data.code == "200") {
    //       this.patientsData.splice(index,1);
    //       Vue.$toast.success("hasta başarıyla silindi");
    //     }
    //   }).catch(err=> {
    //     console.log(err);
    //     Vue.$toast.error(err);
    //   })
    // }
    
  }
}
</script>

<style scoped>
  .blink {
      animation: blink 2s steps(5, start) infinite;
      -webkit-animation: blink 1s steps(5, start) infinite;
    }
    @keyframes blink {
      to {
        visibility: hidden;
      }
    }
    @-webkit-keyframes blink {
      to {
        visibility: hidden;
      }
    }
    @import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans:wght@300&display=swap');
/* *{
  font-family: 'IBM Plex Sans', sans-serif;
} */
.a {
  margin: 0 !important;
}

.first-item{
  background-color: rgb(104, 187, 104);
} 
.x{
    background: -webkit-linear-gradient(left, rgb(102, 6, 19),rgb(168, 60, 74));;
  }
  .x:hover{
    cursor: pointer;
    text-decoration: none;
    color:rgb(146, 109, 109);
  }
  .question {
    background: rgb(255, 255, 255);
    border-bottom-right-radius: 10px;
    border-bottom-left-radius: 10px;
    box-shadow: 0 15px 15px rgba(0, 0, 0, 0.1);
    padding: 1rem 1rem 0 1rem;
    margin-bottom: 2rem;
    border-top:none !important
    
  }

  .question-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-transform: capitalize;
    padding-bottom: 1rem;
    color: black;
    font-family: 'PT Sans Narrow', sans-serif;
  }
  .question-btnm {
    font-size: 1.5rem;
    background: transparent;
    border-color: transparent;
    cursor: pointer;
    color: rgb(214, 245, 214);
    transition: all 0.3s linear;;
  }
  .question-btnm:hover {
    transform: rotate(90deg);
  }
  .question-text {
    padding: 1rem 0 1.5rem 0;
    border-top: 2px solid rgb(149, 224, 153); 
    color: rgb(56, 53, 38);
    font-family: 'PT Sans Narrow', sans-serif;
    position: relative;
  }
  .question-text p {
    margin-bottom: 0;
  }
  /* hide text */
  .question-text {
    display: none;
  }
  .show-text .question-text {
    display: block;
  }
  .minus-icon {
    display: none;
  }
  .show-text .minus-icon {
    display: inline;
  }
  .show-text .plus-icon {
    display: none;
  }
  .chapter-one{
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
  }
  .question:hover{
    cursor: pointer;
  }
  
  .minus-icon {
    display: none;
  }
  .show-text .minus-icon {
    display: inline;
  }
  .show-text .plus-icon {
    display: none;
  }
  .chapter-one{
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
  }
  .field-two{
    height: auto;
  }
  .field-one{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .ciz{
    border-bottom: 3px solid black;
  }
  .card-head {
    background-color: rgb(100, 194, 100);
  }
  
  .question-title {
    /* display: flex;
    justify-content: space-between;
    align-items: center;
    text-transform: capitalize;
    padding-bottom: 1rem; */
    
  }
  .show-text .question-text {
    display: block;
  }
  .minus-icon {
    display: none;
  }
  .show-text .minus-icon {
    display: inline;
  }
  .show-text .plus-icon {
    display: none;
  }
  .question-btn {
    font-size: 1.5rem;
    background: transparent;
    border-color: transparent;
    cursor: pointer;
    color: rgb(10, 10, 8);
    transition: var(--transition);
  }
  @media screen and (min-width: 768px) {
    i {
      font-size: 25px;
    }
  }
  @media screen and (max-width: 768px) {
    i {
      font-size: 20px;
    }
  }
  
  .form-control::placeholder { /* Chrome, Firefox, Opera, Safari 10.1+ */
    color: black;
    opacity: 1; /* Firefox */
  }

  .form-control:-ms-input-placeholder { /* Internet Explorer 10-11 */
    color: black;
  }

  .form-control::-ms-input-placeholder { /* Microsoft Edge */
    color: black;
  }
  .custom-select {
    position: relative;
    font-family: Arial;
    color:black;
  }

  .custom-select select {
    display: none; /*hide original SELECT element: */
  }

  .select-selected {
    background-color: DodgerBlue;
  }

  /* Style the arrow inside the select element: */
.select-selected:after {
  position: absolute;
  content: "";
  top: 14px;
  right: 10px;
  width: 0;
  height: 0;
  border: 6px solid transparent;
  border-color: #fff transparent transparent transparent;
}

/* Point the arrow upwards when the select box is open (active): */
.select-selected.select-arrow-active:after {
  border-color: transparent transparent #fff transparent;
  top: 7px;
}

/* style the items (options), including the selected item: */
.select-items div,.select-selected {
  color: #bb1e1e;
  padding: 8px 16px;
  border: 1px solid transparent;
  border-color: transparent transparent rgba(0, 0, 0, 0.1) transparent;
  cursor: pointer;
}

/* Style items (options): */
.select-items {
  position: absolute;
  background-color: DodgerBlue;
  top: 100%;
  left: 0;
  right: 0;
  z-index: 99;
}

/* Hide the items when the select box is closed: */
.select-hide {
  display: none;
}

.select-items div:hover, .same-as-selected {
  background-color: rgba(0, 0, 0, 0.1);
}

</style>
  
