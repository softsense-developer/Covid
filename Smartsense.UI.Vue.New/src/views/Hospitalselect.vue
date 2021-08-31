<template>
<div>
   
     <Navbar/>
      <div class="container">
        <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="account-update w-100">
              <div class="container" >
                
                <div class="row">
                  <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
                    <h5 style="text-transform:capitalize">{{name}}</h5>
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
                    <button class="btn btn-success w-100" @click="postHospital()">Gönder</button>
                  </div>
                </div>
              </div>    
            </div>
          </div>
        
       
       
      </div>
  </div>  

</template>

<script>
import Vue from 'vue'
import Navbar from '@/components/Navbaradmin'
import axios from 'axios'
export default {
    name:'hospitalselect',
    data() {
        return{
            name:this.$route.params.name,
            proId:this.$route.params.Pid,
            hospitals:[],
            hospitalId:'',
            userId:''
        }
    },
    components:{
      Navbar
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
            if(res.data.code == "200") {
            Vue.$toast.success('hasta başarıyla supervisor rolüne atanmıştır')
            }
          }).catch(err=> {
          Vue.$toast.error(err);
            })
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
/* @import '../assets/css/content.css'; 
@import '../assets/css/login.css';  */

</style>