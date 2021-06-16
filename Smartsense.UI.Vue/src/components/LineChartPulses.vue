<template>
  <div class="container">
    <div class="row">
      <a  class="badge text-white mt-1 last-weeks" @click="getFavoriotLastWeek()" style="cursor:pointer;border:none;background:grey;">Son bir hafta</a>
      <a  class="badge text-white mt-1 ml-1 last-threes" @click="getFavoriotLastThreeDay()" style="cursor:pointer;border:none;background:grey;">Son üç gün</a>
      <a  class="badge text-white mt-1 ml-1 last-days" @click="getFavoriotLastDay()" style="cursor:pointer;border:none;background:grey;">Dün</a>
      <a  class="badge text-white mt-1 ml-1 todays" @click="getFavoriotToday()" style="cursor:pointer;border:none;background:grey;">Bugün</a>  
    </div>
    <div v-if="pageMode==0">
      <div class="container">
        <div class="row">
          <!-- <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="width:800px;height:200px;align-items:center;">
            <h4>Güncel veriniz bulunmamaktadır</h4>
          </div> -->
        </div>
      </div>
    </div>
    <canvas id="myChartPulses" width="800" height="300" v-if="pageMode==1"></canvas>
  </div>
</template>

<script>
import axios from 'axios'
import Chart from 'chart.js';
export default {
  data() {
   return {
     pulses:[],
     time:[],
     chart:"",
     lastWeek:"",
     lastThree:"",
     lastDay:"",
     today:"",
     pageMode:1
   }
  },
  mounted() {
    this.ChartCreate();
  },
  methods: {

    getLastWeek() {
        var today = new Date();
        var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 5);
        this.lastWeek=lastWeek;
        console.log(this.lastWeek);
    },
    getToday() {
        var today = new Date();
        var todayx = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        this.today=todayx;
        console.log(this.today);
    },
    getLastThreeDay() {
        var today = new Date();
        var lastThree = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 3);
        this.lastThree=lastThree;
        console.log(this.lastThree);
    },
    getLastDay() {
        var today = new Date();
        var lastDay = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
        this.lastDay=lastDay;
        console.log(this.lastDay);
    },
    getFavoriotLastWeek() {
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#77c0d8'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD';
      this.getLastWeek()
      var now=new Date();
      this.GlobalRequiest(this.lastWeek,now)
    },
    getFavoriotLastThreeDay() {
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#77c0d8'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD'
      this.getLastThreeDay()
      var now=new Date();
      this.GlobalRequiest(this.lastThree,now)
    },
    getFavoriotLastDay() {
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#77c0d8'
      today.style.backgroundColor='#BDBDBD'
      this.getLastDay()
      var now=new Date();
      this.GlobalRequiest(this.lastDay,now)
    },
    getFavoriotToday() {
      var todayx=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      todayx.style.backgroundColor='#77c0d8'
      this.getToday()
      var now=new Date();
      this.GlobalRequiest(this.today,now)
    },
    GlobalRequiest(x,y) {
      this.pulses=[];
      this.time=[];
      var token=localStorage.getItem('token');
      var url="http://api.smartsense.com.tr/api/value/gethistorydaterange";
      var jsonObject={
          "oldDate": x,
          "newDate": y
        }
        console.log(jsonObject);
        axios.post(url,jsonObject,{
        headers:{'Authorization': `Bearer ${token}`}
          }).then(res=> {
          console.log(res.data);
          res.data.pulses.forEach(element => {
          this.pulses.push(element.dataValue);
          this.time.push(element.saveDate.slice(11,19));
        });
          this.pageMode=1;
          this.updateChart();
        })
    },
    ChartCreate() {
      var ctx=document.getElementById('myChartPulses').getContext('2d');
      this.chart=new Chart(ctx, {
        type:'line',
        data: {
          labels:[],
          datasets:[{
            label:'firstDataSet',
            backgroundColor:'transparent',
            borderColor:'#77c0d8',
            data:[]
          }]
          
        }
      })
    },
    updateChart() {
      this.chart.data.datasets[0].data = this.pulses;
      this.chart.data.labels = this.time;
      this.chart.update();
    }

    
  }

}
</script>

<style>


</style>