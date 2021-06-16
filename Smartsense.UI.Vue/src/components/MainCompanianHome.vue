<template>
<div>
  <div class="mydiv" style="width:1px ;height:1px;"></div>
 <main class="page-content">
    <div class="row d-flex justify-content-center">
      <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
        <h5>Hasta Verileri</h5>
      </div>
    </div>
    <div class="row d-flex justify-content-start">
      <div class="col-sm-12 col-md-12 col-lg-12"  v-for="patients of patients" :key="patients.id" style="padding-left: 0 !important;padding-right: 0 !important;">
        <!-- aslında v-bind (:) yerine kullanılabilirdi -->
        <div class="question" :id="patients.id">   
        <!-- question title -->
          <div class="question-title d-flex" @click="AddressDisplay(patients.id)" style="align-items:center" >
            <div class="" style=";height:30px">
              <strong><span style="font-size:12px">{{patients.name}} {{patients.surname}}</span></strong>
            </div>
            <div class=" d-flex" style="height:30px;">
              <div class="d-flex align-items:center;">
                <span class="pointer" v-if="patients.pulseWarning==1" ><i class="fas fa-heart" style="color:#EF9A9A"></i></span>
                <span class="pointer-fast" v-if="(patients.pulseWarning==0)||(patients.pulseWarning==2)" ><i class="fas fa-heart" style="color:#E57373"></i></span>
              </div>
            <div class="d-flex justify-content-center"></div>
            <div class="d-flex" style="align-items:center">
              <i class="fas fa-arrow-up mr-1" style="font-size:12px;color:#E57373" v-if="patients.pulseWarning==2"></i>
              <!-- <i class="fas fa-arrow-right mr-1" style="font-size:12px;color:#9E9E9E" v-if="patients.pulseWarning==1"></i> -->
              <i class="fas fa-arrow-down mr-1" style="font-size:12px;color:#E57373" v-if="patients.pulseWarning==0"></i>
              <span class="" style="">{{patients.pulse}}</span>
            </div>
            </div>
            <div class="d-flex" style="align-items:center;">
              <div class="d-flex align-items:center;">
                <span class="pointer" v-if="patients.temperatureWarning==1" ><i class="fas fa-burn" style="color:#EF9A9A"></i></span>
                <span class="pointer-fast" v-if="(patients.temperatureWarning==0)||(patients.temperatureWarning==2)" ><i class="fas fa-burn" style="color:#E57373"></i></span>
              </div>
                <i class="fas fa-arrow-up mr-1" style="font-size:12px;color:#E57373" v-if="patients.temperatureWarning==2"></i>
                <!-- <i class="fas fa-arrow-right mr-1" style="font-size:12px;color:#9E9E9E" v-if="patients.temperatureWarning==1"></i> -->
                <i class="fas fa-arrow-down mr-1" style="font-size:12px;color:#E57373" v-if="patients.temperatureWarning==0"></i>
                <span class="" style="">{{(patients.temperature).toFixed(2)}}</span>
            </div>
            <div class="d-flex" style="align-items:center;">
              <div class="d-flex align-items:center;">
                <span class="pointer" v-if="patients.oxygenWarning==1" ><i class="fas fa-tint" style="color:#90CAF9"></i></span>
                <span class="pointer-fast" v-if="(patients.oxygenWarning==0)||(patients.oxygenWarning==2)" ><i class="fas fa-tint" style="color:#64B5F6"></i></span>
              </div>
                <i class="fas fa-arrow-up mr-1 text-danger" style="font-size:12px;color:#E57373" v-if="patients.oxygenWarning==2"></i>
                <!-- <i class="fas fa-arrow-right mr-1" style="font-size:12px;color:#9E9E9E" v-if="patients.oxygenWarning==1"></i> -->
                <i class="fas fa-arrow-down mr-1" style="font-size:12px;color:#E57373" v-if="patients.oxygenWarning==0"></i>
                <span class="" style="">{{patients.oxygen}}</span>
            </div>
              <button type="button" class="question-btn" style="color:rgb(8,113,101)">
                <span class="plus-icon">
                  <i class="far fa-plus-square"></i>
                </span>
                <span class="minus-icon">
                  <i class="far fa-minus-square"></i>
                </span>
            </button>
          </div>
          <!-- doctor delete warnings -->
        <!-- question text -->
          <div class="question-text">
            <div class="container-fluid">
              <div class="row">
                <div class="col-lg-6 col-sm-12 col-md-6">
                  <div class=" text-danger">
                    <span class="btn text-light" style="pointer-events:none;background:#E57373;border:none;font-size:12px">{{patients.diagnosis}}</span>
                    <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patients.patientStatus==0">evde karantinada</span>
                    <span class="btn ml-2 text-dark" style="pointer-events:none;background:#DCE775;border:none;font-size:12px" v-if="patients.patientStatus==1">hastanede karantinada</span>
                  </div>
                </div>
                <div class="col-sm-12 col-md-6 col-lg-6 abcd rows d-flex justify-content-end">
                  <div class="text-danger">
                    <strong><a class="text-dark" @click="goTodetail(patients.id)" style="font-size:12px;" >Hasta Geçmişini Görüntüle</a></strong><br>
                  </div>
                </div>
              </div>
            </div>  
          </div>
        </div>
      </div>
    </div>
     <!-- <div v-if="patients.length==0">
         <blockquote class="blockquote text-center">
         
          <footer class="blockquote-footer">Kayıtlı hastanız bulunmamaktadır <cite title="Source Title"></cite></footer>
         
        </blockquote>
     </div> -->
   </main>
 </div>
</template>

<script>
import axios from 'axios'

export default {

  data() {
    return {
      patients:[],
      name:'',
      pulses:[],
      temp:[],
      o2:[],
      pulseWarning:null,
      oxygenWarning:null,
      patientStatus:'',
      temperatureWarning:null,  
    }
  },

  mounted() {
    this.getPatients();
  },

  methods: { 

    getPatients() {
      var token=localStorage.getItem('token');
      var url='http://api.smartsense.com.tr/api/companion/getCompanionpatients'
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
          console.log(res);
          res.data.patientsData.forEach(element => {
          this.patients.push(element);    
        });
        console.log(this.patients);
      })
    },

    // routeId(id){
    //   this.pulses.length=null
    //   var url='http://api.smartsense.com.tr/api/doctor/getpatientdata'+`/${id}`
    //   var token=localStorage.getItem('token');
    //   console.log(url);
    //   axios.get(url,{
    //     headers:{'Authorization': `Bearer ${token}`}
    //   }).then(res=> {
    //       console.log(res);
    //       this.name=res.data.name;
    //       this.temperatureWarning=res.data.temperatureWarning;
    //       this.oxygenWarning=res.data.oxygenWarning;
    //       this.temperatureWarning=res.data.temperatureWarning;
    //       console.log(this.name);
    //       res.data.pulses.forEach(el=>{
    //         this.pulses.push(el);
    //       })
    //     })
    // },

    

      goTodetail(prodId) {
        this.$router.push({name:'details',params:{Pid:prodId}})
      },

    AddressDisplay(id) {
      console.log(id);
      var title=document.querySelectorAll('.question');
      title.forEach(element=> {
        if(element.id==id) {
          element.classList.toggle('show-text');
        }
      })
    },

    reload() {
      window.location.reload();
    },
  }
}
</script>

<style scoped>
@import '../assets/css/question.css'; 

@media screen and (max-width: 768px) {
  .abcd{
    margin-top:5%;
  }
  .rows{
    display: flex !important;
    justify-content: start !important;
  }
}
  .danger{
    position: absolute;
    right: 0;
  }




</style>


