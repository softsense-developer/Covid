<template>
  <div class="container">
    <div class="row">
      <a  class="badge text-white mt-1 last-week" @click="getFavoriotLastWeek()" style="cursor:pointer;border:none;background:grey;">Son bir hafta</a>
      <a  class="badge text-white mt-1 ml-1 last-three" @click="getFavoriotLastThreeDay()" style="cursor:pointer;border:none;background:grey;">Son üç gün</a>
      <a  class="badge text-white mt-1 ml-1 last-day" @click="getFavoriotLastDay()" style="cursor:pointer;border:none;background:grey;">Dün</a>
      <a  class="badge text-white mt-1 ml-1 today" @click="getFavoriotToday()" style="cursor:pointer;border:none;background:grey;">Bugün</a>  
    </div>
    <div v-if="pageMode==0">
      <div class="container">
        <div class="row">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="width:800px;height:200px;align-items:center;">
            <h4>Güncel veriniz bulunmamaktadır</h4>
          </div> 
        </div>
      </div>
    </div>
    <canvas id="myChartPulsess" width="800" height="300" v-if="pageMode==1"></canvas>
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
     pageMode:1,
     todayNight:'',
     lastWeekNight:'',
     lastThreeNight:'',
     lastDayNight:''
   }
  },
  mounted() {
    this.ChartCreate();
    this.getFavoriotToday();
  },
  
  methods: {

    getLastWeek() {
        var today = new Date();
        var lastWeekNight = new Date(today.getFullYear(), today.getMonth(), today.getDate()- 6,3,0,0);
        var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate(),26,59,59);
        this.lastWeek=lastWeek;
        this.lastWeekNight=lastWeekNight
        console.log(this.lastWeek);
    },
    getToday() {
        var today = new Date();
        var todayNight=new Date(today.getFullYear(),today.getMonth(),today.getDate(),3,0,0)
        var todayx = new Date(today.getFullYear(), today.getMonth(), today.getDate() ,26,59,59);
        // var todayx = new Date(2011, 0, 1, 2, 3, 4, 567);
        this.today=todayx;
        this.todayNight=todayNight;
        console.log(this.today);
        console.log(todayNight);
    },
    getLastThreeDay() {
        var today = new Date();
        var lastThreeNight = new Date(today.getFullYear(), today.getMonth(), today.getDate()- 2,3,0,0);
        var lastThree=new Date(today.getFullYear(), today.getMonth(), today.getDate() ,26,59,59);
        this.lastThree=lastThree;
        this.lastThreeNight=lastThreeNight;
        console.log(this.lastThree);
    },
    getLastDay() {
        var today = new Date();
        var lastDayNight = new Date(today.getFullYear(), today.getMonth(), today.getDate()-1,3,0,0);
        var lastDay=new Date(today.getFullYear(), today.getMonth(), today.getDate()-1 ,26,59,59);
        this.lastDay=lastDay;
        this.lastDayNight=lastDayNight;
        console.log(this.lastDay);
    },
    getFavoriotLastWeek() {
      var today=document.querySelector('.today');
      var last_three=document.querySelector('.last-three');
      var last_week=document.querySelector('.last-week');
      var last_day=document.querySelector('.last-day');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#77c0d8'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD';
      this.getLastWeek()
      this.GlobalRequiest(this.lastWeekNight,this.lastWeek)
    },
    getFavoriotLastThreeDay() {
      var today=document.querySelector('.today');
      var last_three=document.querySelector('.last-three');
      var last_week=document.querySelector('.last-week');
      var last_day=document.querySelector('.last-day');
      last_three.style.backgroundColor='#77c0d8'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD'
      this.getLastThreeDay()
      this.GlobalRequiest(this.lastThreeNight,this.lastThree)
    },
    getFavoriotLastDay() {
      var today=document.querySelector('.today');
      var last_three=document.querySelector('.last-three');
      var last_week=document.querySelector('.last-week');
      var last_day=document.querySelector('.last-day');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#77c0d8'
      today.style.backgroundColor='#BDBDBD'
      this.getLastDay()
      this.GlobalRequiest(this.lastDayNight,this.lastDay)
    },
    getFavoriotToday() {
      var todayx=document.querySelector('.today');
      var last_three=document.querySelector('.last-three');
      var last_week=document.querySelector('.last-week');
      var last_day=document.querySelector('.last-day');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      todayx.style.backgroundColor='#77c0d8'
      this.getToday()
      this.GlobalRequiest(this.todayNight,this.today)
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
          res.data.oxygen.forEach(element => {
          this.pulses.push(element.dataValue);
          this.time.push(element.saveDate.slice(11,19));
        });
        if(this.pulses.length==null){
          this.pageMode=0;
        }
          this.pageMode=1;
          this.updateChart();
        })
    },
    ChartCreate() {
      var ctx=document.getElementById('myChartPulsess').getContext('2d');
      this.chart=new Chart(ctx, {
        type:'line',
        options: {
          elements: {
          point: {
              radius: 1.8
          }
        },
        scales: {
          yAxes: [{
            gridLines: {
              display:false,
              drawBorder: false,
            },
            display: true,
            ticks: {
              beginAtZero: true,
              min: 85,
              max: 100,
              stepSize: 1 // 1 - 2 - 3 ...,
            }
          }],
          xAxes: [{
            gridLines: {
            display:false,
            drawBorder: false,
            },
          }]
        } 
        },

        data: {
          labels:[],
          datasets:[{
            label:'SpO²',
            backgroundColor:'transparent',
            borderColor:'rgb(119,192,216)',
            data:[]
          }]
        },
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