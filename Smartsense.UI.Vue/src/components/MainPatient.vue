<template>
  <div>  
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container" >
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Hasta Bilgileri</h5>
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" v-if="pageMode==0">
            <scaling-squares-spinner class="text-info mt-5"
            :animation-duration="1000"
            :size="75"
            :color="'#0f796c'"
          />
          </div>
        </div>
        <div class="row mt-3" v-if="pageMode==1">
          <div class="col-sm-12 col-md-4 col-lg-4 mt-3">
            <div class="small-box" style="background:#77c0d8;cursor:pointer;">
              <div class="inner">
                <h5 style="color:#F5F5F5;">{{lastValueOxygen}}<sup style="font-size: 15px">%</sup></h5>
                <p style="color:#F5F5F5;">{{dateoxygen}}</p>
                <p style="color:#F5F5F5;">{{dateoxygentwo}}</p>
              </div>
              <div class="icon">
                <i class="fas fa-tint" style="color:#F5F5F5;" ></i>
              </div>
            </div>
          </div>
          <div class="col-sm-12 col-md-4 col-lg-4 mt-3">
            <div class="small-box" style="background:#FF7043;cursor:pointer;">
              <div class="inner">
                <h5 style="color:#F5F5F5;;">{{lastValueTemp.toFixed(1)}}<sup style="font-size: 15px">C°</sup></h5>
                <p style="color:#F5F5F5;;">{{datetemp}}</p>
                <p style="color:#F5F5F5;;">{{datetemptwo}}</p>
              </div>
              <div class="icon">
                <i class="fas fa-temperature-low" style="color:#F5F5F5;"></i>
              </div>
            </div>
          </div>
          <div class="col-sm-12 col-md-4 col-lg-4 mt-3">
            <div class="small-box" style="background:#EF5350;cursor:pointer;">
              <div class="inner">
                <h5 style="color:#F5F5F5;">{{lastValuePulses}}<sup style="font-size: 10px">BPM</sup></h5>
                <p style="color:#F5F5F5;">{{datePulses}}</p>
                <p style="color:#F5F5F5;">{{datePulsesTwo}}</p>
              </div>
              <div class="icon">
                <i class="fas fa-heartbeat" style="color:#F5F5F5;"></i>
              </div>
            </div>
          </div>
        </div>
        <div class="row mt-4" v-if="pageMode==1">
          <div class="col-sm-12 col-md-12 col-lg-12 mt-2">
            <div class="card"><line-chart></line-chart></div>
          </div>
          <div class="col-sm-12 col-md-12 col-lg-12 mt-5">
            <div class="card"><line-chart-temp></line-chart-temp></div>
          </div>
          <div class="col-sm-12 col-md-12 col-lg-12 mt-5">
            <div class="card"><line-chart-pulses></line-chart-pulses></div>
          </div>  
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
import LineChart from './LineChart.vue'
import LineChartPulses from './LineChartPulses.vue'
import LineChartTemp from './LineChartTemp.vue'
export default {
  name:"mainSide",
  data() {
    return {
      oxygen:[],
      time:[],
      pulses:[],
      temp:[],
      timetemp:[],
      timePulses:[],
      lastValueOxygen:"",
      lastTimeOxygen:"",
      lastValuePulses:"",
      lastTimePulses:"",
      lastValueTemp:"",
      lastTimeTemp:"",
      flashmessage:false,
      datePulses:"",
      datePulsesTwo:"",
      datetemp:"",
      datetemptwo:"",
      dateoxygen:"",
      dateoxygentwo:"",
      pageMode:0
    }
  },

  components: {
    LineChart,
    LineChartTemp,
    LineChartPulses,
    ScalingSquaresSpinner
  },

  mounted() {
    this.getfavoriot();
    this.getfavoriotPulses();
    this.getfavoriotTemp();
    setTimeout(() => this.pageMode=1, 2000);
  },
  methods: {
    deneme() {
      this.flashmessage=true
    },

    getfavoriot() {
        var token=localStorage.getItem('token');
        var url="http://api.smartsense.com.tr/api/value/gethistory";
        axios.get(url,{
          headers:{'Authorization': `Bearer ${token}`}
          }).then(res=> {
            console.log(res.data);
            res.data.oxygen.forEach(element => {
            this.oxygen.push(element.dataValue);
            this.time.push(element.saveDate);
        });
        this.lastValueOxygen=this.oxygen.pop();
        this.lastTimeOxygen=this.time.pop();
        this.dateoxygen=this.lastTimeOxygen.slice(0,10);
        this.dateoxygentwo=this.lastTimeOxygen.slice(11,19);
        console.log(this.lastTime);
      })
    },

    getfavoriotPulses() {
        var token=localStorage.getItem('token');
        var url="http://api.smartsense.com.tr/api/value/gethistory";
        axios.get(url,{
          headers:{'Authorization': `Bearer ${token}`}
          }).then(res=> {
            console.log(res.data);
            res.data.pulses.forEach(element => {
            this.pulses.push(element.dataValue);
            this.timePulses.push(element.saveDate);
            // this.datePulses.push(element.saveDate.slice(11,19));
        });
        this.lastValuePulses=this.pulses.pop();
        this.lastTimePulses=this.timePulses.pop();
        this.datePulses=this.lastTimePulses.slice(0,10);
        this.datePulsesTwo=this.lastTimePulses.slice(11,19)
      })
    },

    getfavoriotTemp() {
        var token=localStorage.getItem('token');
        var url="http://api.smartsense.com.tr/api/value/gethistory";
        axios.get(url,{
          headers:{'Authorization': `Bearer ${token}`}
          }).then(res=> {
            console.log(res.data);
            res.data.temperatures.forEach(element => {
            this.temp.push(element.dataValue);
            this.timetemp.push(element.saveDate);
        });
        this.lastValueTemp=this.temp.pop();
        this.lastTimeTemp=this.timetemp.pop();
        this.datetemp=this.lastTimeTemp.slice(0,10);
        this.datetemptwo=this.lastTimeTemp.slice(11,19)
      })
    },
  }
}
</script>

<style scoped>
@import '../assets/css/smallbox.css';
@import '../assets/css/toast.css'




</style>

