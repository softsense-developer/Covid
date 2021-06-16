<template>
  <div>
  <p>merhaba dünya</p>
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
        <div class="row mt-5">
          <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="account-update ">
              <LineChartxx :chart-data='datacollectionThree' class="mt-3" :options="{responsive: true, maintainAspectRatio: false}"></LineChartxx> 
            </div>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="account-update ">
              <LineChart :chart-data='datacollection' :options="{responsive: true, maintainAspectRatio: false}"></LineChart> 
            </div>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="account-update">
              <LineChartx :chart-data='datacollectionTwo' :options="{responsive: true, maintainAspectRatio: false}" ></LineChartx>
            </div>
          </div>
        </div>
      </div>
                

    </main>
  </div>  
</div>
</template>
<script>
import LineChartxx from '../components/favarite.js'
import LineChart from '../components/favarite.js'
import LineChartx from '../components/favarite.js'
import navbar from '../components/navbar.vue'
import axios from 'axios'

export default {
name:'SupDetails',
  components:{
    navbar,
    LineChartxx,
    LineChart,
    LineChartx
  },
    data(){
      return{
      loaded:false,
      datacollectionThree:null,
      datacollectionTwo:null,
      datacollection:null,
      proId:this.$route.params.Pid,
      title:"detailsSup",
      pulses:[],
      timePulses:[],
      oxygen:[],
      timeOxygen:[],
      temperatures:[],
      timeTemperatures:[],
      name:'',
      surname:''

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
      var url='http://api.smartsense.com.tr/api/supervisor/getpatient'+`/${id}`
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
      this.datacollectionThree={
      labels:this.timePulses,
      datasets:[{
        label:'nabız',
        backgroundColor:'rgba(255,255,0,0.2)',
        borderColor:'lightblue',
        pointBackgroundColor:'orange',
        borderWidth:1,
        pointBorderColor:'orange',
        data:this.pulses,
        }], 
      },{responsive: true, maintainAspectRatio: false}
    },
    fillData(){
      this.datacollection={
      labels:this.timeOxygen,
      datasets:[{
        label:'Oksijen(°C )',
        backgroundColor:'rgba(0,0,255,0.2)',
        borderColor:'lightblue',
        pointBackgroundColor:'blue',
        borderWidth:1,
        pointBorderColor:'blue',
        with:'auto',
        data:this.oxygen,
      }], 
      },{responsive: true, maintainAspectRatio: false}
    },
    fillDataTwo(){
      this.datacollectionTwo={
      labels:this.timeTemperatures,
        datasets:[{
        label:'Sıcaklık(°C )',
        backgroundColor:'rgba(255,0,0,0.2)',
        borderColor:'red',
        pointBackgroundColor:'red',
        borderWidth:1,
        pointBorderColor:'red',
        data:this.temperatures,
      }], 
      },{responsive: true, maintainAspectRatio: false}
    },
  },
  
}
</script>

<style>

</style>