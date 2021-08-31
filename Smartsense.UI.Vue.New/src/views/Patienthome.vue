<template>
  <div>  
     <Navbar/>
     <h1 class="subheading grey--text">Anasayfa</h1>
      <div class="container" >
        <div class="row">
          <div class="col-sm-12 col-md-12 col-lg-12 mt-2">
            <line-chart></line-chart>
          </div>
          <div class="col-sm-12 col-md-12 col-lg-12 mt-5">
            <line-chart-temp></line-chart-temp>
          </div>
          <div class="col-sm-12 col-md-12 col-lg-12 mt-5">
            <line-chart-pulses></line-chart-pulses>
          </div>  
        </div>
      </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar'
// import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
import LineChart from '../components/LineChart.vue'
import LineChartPulses from '../components/LineChartPulses.vue'
import LineChartTemp from '../components/LineChartTemp.vue'
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
    // ScalingSquaresSpinner,
    Navbar
  },

  mounted() {
    this.getfavoriot();
    this.getfavoriotPulses();
    this.getfavoriotTemp();
    // setTimeout(() => this.pageMode=1, 2000);
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
.card {
    overflow: hidden;
    width: 100%;
    background: #fff;
    padding: 30px;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);
  }




</style>

