<template>
  <div>
    <div class="page-wrapper chiller-theme toggled">
      <a id="show-sidebar" class="btn btn-sm btn-dark" @click="sidebarOpen()">
        <i class="fas fa-bars"></i>
      </a>
    <navbar></navbar>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
        <div class="row">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid #607D8B">
            <h5 style="color:#607D8B;text-transform: capitalize;">{{name}} {{surname}} Hastasına Ait Grafikler</h5>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="card">
              <canvas id="myChart1" width="800" height="400"></canvas>
            </div>
          </div>
          <div class="col-sm-12 col-md-12 col-lg-12 mt-3">
            <div class="card">
              <canvas id="myChart2" width="800" height="400"></canvas>
            </div>
          </div>
          <div class="col-sm-12 col-md-12 col-lg-12 mt-3">
            <div class="card">
              <canvas id="myChart3" width="800" height="400"></canvas>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>  
</div>
</template>

<script>
import Chart from 'chart.js';
import navbar from '../components/navbar.vue'
import axios from 'axios'

export default{
  name:'details',
  components:{
    navbar,
  
  },
    data(){
      return{
      loaded:false,
      datacollectionThree:null,
      datacollectionTwo:null,
      datacollection:null,
      proId:this.$route.params.Pid,
      title:"details",
      pulses:[],
      timePulses:[],
      oxygen:[],
      timeOxygen:[],
      temperatures:[],
      timeTemperatures:[],
      name:'',
      surname:'',
      myChart1:'',
      myChart2:'',
      myChart3:'',

    }   
  },
    mounted(){
      this.routeId(this.proId);
    },
  methods:{
    sidebarOpen(){
      var openSide=document.querySelector('.page-wrapper');
      openSide.classList.add('toggled')
    },
    routeId(id){
      var url='http://api.smartsense.com.tr/api/doctor/getpatientdata'+`/${id}`
      var token=localStorage.getItem('token');
      console.log(url);
      axios.get(url,{
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
        console.log(res);
        this.name=res.data.name;
        this.surname=res.data.surname;
        console.log(this.name);
        res.data.pulses.forEach(el=>{
          this.pulses.push(el.dataValue);
          this.timePulses.push(el.saveDate.slice(11,19));
        })
        res.data.oxygen.forEach(el=>{
          this.oxygen.push(el.dataValue);
          this.timeOxygen.push(el.saveDate.slice(11,19));
        })
        res.data.temperatures.forEach(el=>{
          this.temperatures.push(el.dataValue);
          this.timeTemperatures.push(el.saveDate.slice(11,19));
        })
        this.fillDataThree()
        this.fillData()
        this.fillDataTwo()
      })
    },
    fillDataThree(){
       var ctx = document.getElementById('myChart3').getContext('2d');
        this.myChart3 = new Chart(ctx, {
        type: 'line',
        data: {
          labels: this.timePulses,
          datasets: [{
            label: 'Nabız',
            data: this.oxygen,
            backgroundColor:"transparent",
            borderColor:"#E53935",
            borderWidth: 2,
          }]
        },
        options: {
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
    },
    fillData(){
       var ctx = document.getElementById('myChart1').getContext('2d');
        this.myChart1 = new Chart(ctx, {
        type: 'line',
        data: {
          labels: this.timeOxygen,
          datasets: [{
            label: 'spO²',
            data: this.oxygen,
            backgroundColor:"transparent",
            borderColor:"#77c0d8",
            borderWidth: 2,
          }]
        },
        options: {
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
    },
    fillDataTwo(){
       var ctx = document.getElementById('myChart2').getContext('2d');
        this.myChart2 = new Chart(ctx, {
        type: 'line',
        data: {
          labels: this.timeTemperatures,
          datasets: [{
            label: 'C°',
            data: this.temperatures,
            backgroundColor:"transparent",
            borderColor:"#FF7043",
            borderWidth: 2,
          }]
        },
        options: {
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
},
  }}
</script>

<style scoped>
@import '../assets/css/content.css'; 
@import '../assets/css/login.css'; 
 /* .small {
     max-width: 900px; 
     margin:  150px auto; 
     max-height: 600px; 
  } */

</style>

