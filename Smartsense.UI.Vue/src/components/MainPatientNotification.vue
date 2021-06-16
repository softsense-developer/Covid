<template>
  <div>
    <main class="page-content" >
      <div class="container">
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">İstekler</h5>
          </div>
        </div>
        <div class="mt-5 text-center d-flex justify-content-center" v-if="pageMode==0">
          <scaling-squares-spinner class="text-info"
            :animation-duration="1000"
            :size="75"
            :color="'#0f796c'"
          />
        </div>
        <div class="row d-flex justify-content-center" v-if="pageMode==1">
        </div>
        <div class="row d-flex justify-content-center xa"  ></div>
      </div>
    </main>
  </div>
</template>

<script>
import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
export default {
    name:'MainNotification',
    data(){
        return{
            patients:[],
            displaypatients:'',
            intervals:'',
            pageMode:0
            
        }
    },
    components:{
      ScalingSquaresSpinner
    },
    
    methods: {
      

      getpatientRequiest() {
       this.patients=[];
       var url=`http://api.smartsense.com.tr/api/patient/getpromotions`
       var token=localStorage.getItem('token');
       axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
       }).then(res=>{
       res.data.connectionRequests.forEach(res=> {

         this.patients.push(res);
       })
       var xa=document.querySelector('.xa');
       this.pageMode=1;
       this.displaypatients=this.patients.map(function(item){
        return `
        <div class="col-sm-12 col-md-12 col-lg-12 mt-3"  style="padding-left: 0 !important;padding-right: 0 !important;">
          <div class='card' id='${item.id}' style='box-shadow: 0px 10px 10px rgba(0,0,0,0.1);border:none;'>
            <div class='card-body d-flex justify-content-between'>
              <div><strong><span style='text-transform: capitalize;'>${item.name} ${item.surname}</span></strong></div>
            <div>
            <div class='d-flex'>
              <i class="fas fa-times btn btn-danger btn-one mr-2" id='${item.id}'></i>
              <i class="fas fa-check btn btn-success btn-two" id='${item.id}'></i>
            </div>
          </div>
        </div>
        </div>
        </div>  `
       });
       this.displaypatients=this.displaypatients.join('');
       xa.innerHTML=this.displaypatients;
       var btn_one=document.querySelectorAll('.btn-one');
       var btn_two=document.querySelectorAll('.btn-two');
       btn_one.forEach(el=>{
         el.addEventListener('click',function(){
          var url='http://api.smartsense.com.tr/api/patient/connectionacceptrefuse'
          var token=localStorage.getItem('token');
          var JsonObjectsx={
           "id":el.id,
           "isAccept": false
         }
       axios.post(url,JsonObjectsx,{
       headers:{'Authorization': `Bearer ${token}`}
       }).then(res=>{
         console.log(res);
          })
         })
       })
       btn_two.forEach(el=>{
         el.addEventListener('click',function(){
          var url='http://api.smartsense.com.tr/api/patient/connectionacceptrefuse'
          var token=localStorage.getItem('token');
          var JsonObjectsx={
           "id":el.id,
           "isAccept": true
         }
       axios.post(url,JsonObjectsx,{
       headers:{'Authorization': `Bearer ${token}`}
       }).then(res=>{
         console.log(res);
          })
         })
       })
      //  this.patients=[];
      })
     },
   },
    

    mounted(){
    //   setTimeout(function(){ this.waiting(); }, 1000);
      this.intervals=setInterval(this.getpatientRequiest, 2000);
    },
    
    destroyed(){
      clearInterval(this.intervals);
    }

}
</script>

<style>

</style>