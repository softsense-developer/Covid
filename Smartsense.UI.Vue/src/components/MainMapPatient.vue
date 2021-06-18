<template>
  <div>
    <main class="page-content">
      <div class="container">
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Konum Bilgileri</h5>
          </div>
        </div>
      <div class="row d-flex justify-content-start rows">
        <div class="col-sm-4 col-md-12 col-lg-12 mt-3 ">
          <div class="account-update">
            <div id="map" style="width:100%;height:500px;"></div>
          </div>
        </div>
      </div>
    </div>
    </main>
  </div>
</template>

<script>

  import axios from 'axios'
  import {Loader} from 'google-maps';
  const options = {/* todo */};
  const loader = new Loader('AIzaSyBD9pknSdrRqlznNiaQG0YahqlEcEMPFL8', options);
  
export default {
    name:"PatientLocation",
    components:{
     
    },
    data(){
      return{
       diziPosition:[],
       latitudeNow:[],
       longitudeNow:[],
       map:{}
      }
    },
     mounted(){
      //   if(localStorage.getItem('roleId')==2){
      //     this.getLocationDoctor();
      //  }
       if(localStorage.getItem('roleId')==1){
          this.getLocationPatient();
       }
       
      
    },
    methods:{
      // getLocationDoctor(){
      //   var token=localStorage.getItem('token');
      //   var url=`http://api.smartsense.com.tr/api/doctor/getdoctorlocations`;
      //   axios.get(url,{
      //   headers:{'Authorization': `Bearer ${token}`}
      //     }).then(res=>{
      //       console.log(res);
      //       res.data.patients.forEach(element => {
      //         console.log(element.latitudeNow+" "+element.longitudeNow);
      //         this.latitudeNow.push(element.latitudeNow)
      //         this.longitudeNow.push(element.longitudeNow)
      //       });
      //       console.log(this.latitudeNow);
      //       console.log(this.longitudeNow);
      //       var latitudeNow=this.latitudeNow[0];
      //       var longitudeNow=this.longitudeNow[0];
      //       loader.load().then(function (google){
      //       const map=new google.maps.Map(document.getElementById('map'), {
      //         center: {lat: latitudeNow, lng: longitudeNow},
      //         zoom: 8,
      //       });
      //       console.log(map);
      //       new google.maps.Marker({
      //         position: {lat: latitudeNow, lng: longitudeNow},
      //         map,
      //       });
      //     });
      //   })
      // },



      
      
      getLocationPatient(){
        var token=localStorage.getItem('token');
        var url=`http://api.smartsense.com.tr/api/patient/getpatientlocation`;
        axios.get(url,{
        headers:{'Authorization': `Bearer ${token}`}
          }).then(res=>{
            console.log(res);
            this.latitudeNow=res.data.latitudeNow,
            this.longitudeNow=res.data.longitudeNow,
            console.log(this.latitudeNow);
            console.log(this.longitudeNow);
            var latitudeNow=this.latitudeNow;
            var longitudeNow=this.longitudeNow;
            loader.load().then(function (google){
            const map=new google.maps.Map(document.getElementById('map'), {
              center: {lat: latitudeNow, lng: longitudeNow},
              zoom: 8,
            });
            console.log(map);
            new google.maps.Marker({
              position: {lat: latitudeNow, lng: longitudeNow},
              map,
              title: localStorage.getItem('name')+" "+localStorage.getItem('surname'),
            });
          });
        })
      }
    }
  }
</script>

<style scoped>


</style>

