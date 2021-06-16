<template>
  <div>
    <main class="page-content">
      <!-- <div class="row d-flex justify-content-center">
        <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
          <h5>Doktor Verileri</h5>
        </div>
      </div> -->
      <div class="row d-flex justify-content-start">
        <div class="col-sm-12 col-md-12 col-lg-12"  v-for="doc of doctors" :key="doc.doktorID" style="padding-left: 0 !important;padding-right: 0 !important;">
        <!-- aslında v-bind (:) yerine kullanılabilirdi -->
          <div class="question" :id="doc.doktorID">   
        <!-- question title -->
            <div class="question-title d-flex" @click="AddressDisplay(doc.doktorID)" style="align-items:center" >
              <div class="" style=";height:30px">
               <strong><span style="font-size:15px">{{doc.doktorAdi}}</span></strong>
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
        <!-- question text -->
            <div class="question-text" style="border-top:none">
              <div class="container">
                <div class="row">
                  <div class="col-12"></div>
                </div>
              </div>
              <div style="overflow-x:auto;">
              <table class="table table-borderless">
                <thead style="text-transform:uppercase;">
                  <tr style="font-size:12px" >
                    <th scope="col">İsim Soyisim</th>
                    <th scope="col">Teşhis</th>
                    <th scope="col">SPO2</th>
                    <th scope="col">Nabız</th>
                    <th scope="col">Ateş</th>
                    <th scope="col">Detay</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="pat of doc.hastalarInfoModels" :key="pat.hastaId">
                    <td>{{pat.name}} {{pat.surname}}</td>
                    <td>{{pat.diagnosis}}</td>
                    <td>
                        <span class="pointer" v-if="pat.oxygenWarning==1"   ><i class="fas fa-tint" style="color:#90CAF9"></i></span>
                        <span class="pointer-fast" v-if="(pat.oxygenWarning==0)||(pat.oxygenWarning==2)" ><i class="fas fa-tint" style="color:#64B5F6"></i></span>
                        <i class="fas fa-arrow-up mr-1 text-danger" v-if="pat.oxygenWarning==2" style="font-size:12px;color:#E57373" ></i>
                        <i class="fas fa-arrow-down mr-1" v-if="pat.oxygenWarning==0" style="font-size:12px;color:#E57373" ></i>
                        <span class="" style="">{{pat.oxygen}}</span>
                    </td>
                    <td>
                        <span class="pointer" v-if="pat.pulseWarning==1"  ><i class="fas fa-heart" style="color:#EF9A9A"></i></span>
                        <span class="pointer-fast" v-if="(pat.pulseWarning==0)||(pat.pulseWarning==2)" ><i class="fas fa-heart" style="color:#E57373"></i></span>
                        <i class="fas fa-arrow-up mr-1" v-if="pat.pulseWarning==2" style="font-size:12px;color:#E57373" ></i>
                        <i class="fas fa-arrow-down mr-1" v-if="pat.pulseWarning==0" style="font-size:12px;color:#E57373"></i>
                        <span class="" style="">{{pat.pulse}}</span>
                    </td>                  
                    <td>
                        <span class="pointer" v-if="pat.temperatureWarning==1" ><i class="fas fa-burn" style="color:#EF9A9A"></i></span>
                        <span class="pointer-fast" v-if="(pat.temperatureWarning==0)||(pat.temperatureWarning==2)"  ><i class="fas fa-burn" style="color:#E57373"></i></span>
                        <i class="fas fa-arrow-up mr-1" style="font-size:12px;color:#E57373" v-if="pat.temperatureWarning==2" ></i>
                        <i class="fas fa-arrow-down mr-1" style="font-size:12px;color:#E57373" v-if="pat.temperatureWarning==0" ></i>
                        <span class="" style="">{{(pat.temperature).toFixed(2)}}</span>
                    </td>
               <td><a class="text-dark" @click="gotodetailsup(pat.hastaId)" style="font-size:15px" ><i class="fas fa-info-circle" ></i></a></td>
                  </tr>
                </tbody>
              </table>
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
    name:'MainSupervisor',
    data(){
        return{
          doctors: [],
          newObject:[],
          doctorNames:[],
          patientsNames:[],
          htmlx:[],
        }
    },
    mounted(){
      this.getDoctors()

    },

    methods:{
      gotodetailsup(prodId) {
      this.$router.push({name:'details',params:{Pid:prodId}})
      },
      
      // gönder(id) {
      //   var a=document.querySelector('.a');
      //   a.innerHTML='';
      //   console.log(id);
      //   this.newObject.forEach(el=>{
      //     if(id==el.doctorId){
      //       const element = document.createElement("div");
      //       // let attr = document.createAttribute("data-id");
      //       // attr.value = id;
      //       // element.setAttributeNode(attr);
      //       element.innerHTML = `
      //       <p>${el.patientName} ${el.patientsurname}</p>
      //      `;
      //      this.htmlx.push(element);
      //      console.log(element);
      //      a.appendChild(element);
      //     }else{
      //        console.log('merhaba');
      //     }
      //   }) 
        
      // },

      AddressDisplay(id){
        console.log(id);
        var title=document.querySelectorAll('.question');
        title.forEach(element=>{
        if(element.id==id){
          element.classList.toggle('show-text');
      }
    })
  },


      getDoctors() {
        var url='http://api.smartsense.com.tr/api/supervisor/getdoctorsinfo'
        var token=localStorage.getItem('token');
        axios.get(url,{
          headers:{'Authorization': `Bearer ${token}`}
        }).then(res=>{
          this.doctors = res.data.doktorInfoModels;
          console.log(this.doctors);
        });
      },
    },

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