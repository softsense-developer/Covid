<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-12 col-md-12 col-lg-12">
        <div class="card">
          <div class="row d-flex justify-content-center">
      <a  class="badge text-white mt-1 last-weeks" @click="getFavoriotLastWeek()" style="cursor:pointer;border:none;background:grey;">Son bir hafta</a>
      <a  class="badge text-white mt-1 ml-1 last-threes" @click="getFavoriotLastThreeDay()" style="cursor:pointer;border:none;background:grey;">Son üç gün</a>
      <a  class="badge text-white mt-1 ml-1 last-days" @click="getFavoriotLastDay()" style="cursor:pointer;border:none;background:grey;">Dün</a>
      <a  class="badge text-white mt-1 ml-1 todays" @click="getFavoriotToday()" style="cursor:pointer;border:none;background:grey;">Bugün</a>  
    </div>
    <div >
      <div class="container">
        <div class="row nodatacardtwo" style="display:none;">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
            <p>Güncel veriniz bulunmamaktadır</p>
          </div> 
        </div>
      </div>
    </div>
    <canvas id="myChartPulses" width="800" height="300" v-if="pageMode==1"></canvas>
  </div>
        </div>
      </div>
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
    this.getFavoriotToday()
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
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#ef5350'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD';
      this.getLastWeek()
      this.GlobalRequiest(this.lastWeekNight,this.lastWeek)
    },
    getFavoriotLastThreeDay() {
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#ef5350'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD'
      this.getLastThreeDay()
      this.GlobalRequiest(this.lastThreeNight,this.lastThree)
    },
    getFavoriotLastDay() {
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#ef5350'
      today.style.backgroundColor='#BDBDBD'
      this.getLastDay()
      this.GlobalRequiest(this.lastDayNight,this.lastDay)
    },
    getFavoriotToday() {
      var today=document.querySelector('.todays');
      var last_three=document.querySelector('.last-threes');
      var last_week=document.querySelector('.last-weeks');
      var last_day=document.querySelector('.last-days');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#ef5350'
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
          if(res.data.oxygen.length == 0) {
              // alert('hello')
              var getir=document.querySelector("#myChartPulses");
              getir.style.display="none";
              var nodatacardx=document.querySelector(".nodatacardtwo");
              nodatacardx.style.display="block"
              
            }else {
              var getirx=document.querySelector("#myChartPulses");
              getirx.style.display="block";
              var nodatacard=document.querySelector(".nodatacardtwo");
              nodatacard.style.display="none"
            }
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
              min: 50,
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
            label:'BPM (Nabız)',
            backgroundColor:'transparent',
            borderColor:'rgb(239,83,80)',
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