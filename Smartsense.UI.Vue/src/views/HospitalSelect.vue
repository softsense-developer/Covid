<template>
<div>
    <div class="page-wrapper chiller-theme toggled">
      <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
        <i class="fas fa-bars"></i>
      </a>
    <navbar></navbar>
     
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
      <div class="row d-flex justify-content-center">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid black;">
             <h5 style="color:#607D8B;text-transform: capitalize;">Doktor Seçimi</h5>
          </div>
        </div>
        <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5">
            <div class="account-update">
              <div class="container" >
                
                <div class="row">
                  <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
                    <h5 style="color:rgb(35,142,130)">{{name}}</h5>
                  </div>
                  <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
                    <select class="form-control" id="exampleFormControlSelect1" style="opacity:0;pointer-events:none;"  >
                      <option value="" >{{proId}}</option>
                    </select>
                  </div>
                  <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
                    <select class="form-control " id="exampleFormControlSelect2" v-model="hospitalId"  >
                     <option value="" >Lütfen Hastane Seçiniz</option> 
                      <option  v-for="o of hospitals" :key="o.id" v-bind:value="o.id" style="text-transform: capitalize;" >{{o.hospitalName}} </option>
                    </select>
                  </div>
                  <div class="col-sm-12 col-md-12 col-lg-12 mt-5">
                    <button class="btn" style="width:100%;background: -webkit-linear-gradient(left, #3ca89c, #00695C);color:white;" @click="postHospital()">Gönder</button>
                  </div>
                </div>
              </div>    
            </div>
          </div>
        
       
       
      </div>
                

    </main>
  </div>  
</div>
</template>

<script>
import navbar from '../components/navbar.vue'
import axios from 'axios'
export default {
    name:'hospitalselect',
    components:{
    navbar,
    
  },
    data() {
        return{
            name:this.$route.params.name,
            proId:this.$route.params.Pid,
            hospitals:[],
            hospitalId:'',
            userId:''
        }
    },
    mounted(){
        this.getHospitals();

    },
    methods:{
    postHospital(){
     var url='http://api.smartsense.com.tr/api/admin/supervisorpromotion';
     var token=localStorage.getItem('token');
     var user={
    "promotionId":this.proId,
    "hospitalId":this.hospitalId,
    }
    console.log(user);
    axios.post(url,user,{
     headers:{
       'Authorization': `Bearer ${token}`
     }
    }).then(res=>{
    console.log(res);
    
    })
    
   },

    sidebarOpen(){
      var openSide=document.querySelector('.page-wrapper');
      openSide.classList.add('toggled')
    },
     getHospitals(){
     var url='http://api.smartsense.com.tr/api/admin/GetAllHospital';
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
      }
    }
}
</script>

<style>
@import '../assets/css/content.css'; 
@import '../assets/css/login.css'; 

</style>