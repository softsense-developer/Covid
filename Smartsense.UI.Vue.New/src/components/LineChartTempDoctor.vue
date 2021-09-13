<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-12 col-md-12 col-lg-12">
        <div class="card">
          <div class="row d-flex justify-content-center">
      <a  class="badge text-white mt-1 last-weekss" @click="getFavoriotLastWeek()" style="cursor:pointer;border:none;background:grey;">Son bir hafta</a>
      <a  class="badge text-white mt-1 ml-1 last-threess" @click="getFavoriotLastThreeDay()" style="cursor:pointer;border:none;background:grey;">Son üç gün</a>
      <a  class="badge text-white mt-1 ml-1 last-dayss" @click="getFavoriotLastDay()" style="cursor:pointer;border:none;background:grey;">Dün</a>
      <a  class="badge text-white mt-1 ml-1 todayss" @click="getFavoriotToday()" style="cursor:pointer;border:none;background:grey;">Bugün</a> 
       <a  class="badge text-white mt-1 ml-1 filter-dayss" @click="changefilterdays()" data-toggle="modal" data-target="#exampleModalTemperature"  style="cursor:pointer;border:none;background:grey;">Tarih aralığı seç</a>
      <div class="modal fade" id="exampleModalTemperature" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <span class="headline">Tarih aralığı seçin</span>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <input type="date" class="form-control" id="birthday" name="birthday" v-model="oldDate" required>
        <input type="date" class="form-control mt-2" id="birthday" name="birthday" v-model="newDate" required>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-danger" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-success" @click="getFavoriotFilter()" >Kaydet</button>
      </div>
    </div>
  </div>
</div>  
    </div>
    <div >
      <div class="container">
        <div class="row nodatacardthree"  style="display:none;">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
            <p>Güncel veriniz bulunmamaktadır</p>
          </div>
        </div>
      </div>
    </div>
    <canvas id="myChartPulsesss" width="800" height="300" v-if="pageMode==1"></canvas>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
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
     lastDayNight:'',
     oldDate:"",
     newDate:""
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
    changefilterdays() {
      var today=document.querySelector('.todayss');
      var last_three=document.querySelector('.last-threess');
      var last_week=document.querySelector('.last-weekss');
      var last_day=document.querySelector('.last-dayss');
      var filter_days=document.querySelector('.filter-dayss')
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      filter_days.style.backgroundColor='#ff7043'
      today.style.backgroundColor='#BDBDBD'
    },
    getFavoriotLastWeek() {
      var filter_days=document.querySelector('.filter-dayss')
      var today=document.querySelector('.todayss');
      var last_three=document.querySelector('.last-threess');
      var last_week=document.querySelector('.last-weekss');
      var last_day=document.querySelector('.last-dayss');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#ff7043'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD';
      filter_days.style.backgroundColor='#BDBDBD'
      this.getLastWeek()
      this.GlobalRequiest(this.lastWeekNight,this.lastWeek)
    },
    getFavoriotLastThreeDay() {
      var filter_days=document.querySelector('.filter-dayss')
      var today=document.querySelector('.todayss');
      var last_three=document.querySelector('.last-threess');
      var last_week=document.querySelector('.last-weekss');
      var last_day=document.querySelector('.last-dayss');
      last_three.style.backgroundColor='#ff7043'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#BDBDBD'
      filter_days.style.backgroundColor='#BDBDBD'
      this.getLastThreeDay()
      this.GlobalRequiest(this.lastThreeNight,this.lastThree)
    },
    getFavoriotLastDay() {
      var filter_days=document.querySelector('.filter-dayss')
      var today=document.querySelector('.todayss');
      var last_three=document.querySelector('.last-threess');
      var last_week=document.querySelector('.last-weekss');
      var last_day=document.querySelector('.last-dayss');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#ff7043'
      today.style.backgroundColor='#BDBDBD'
      filter_days.style.backgroundColor='#BDBDBD'
      this.getLastDay()
      this.GlobalRequiest(this.lastDayNight,this.lastDay)
    },
    getFavoriotToday() {
      var filter_days=document.querySelector('.filter-dayss')
      var today=document.querySelector('.todayss');
      var last_three=document.querySelector('.last-threess');
      var last_week=document.querySelector('.last-weekss');
      var last_day=document.querySelector('.last-dayss');
      last_three.style.backgroundColor='#BDBDBD'
      last_week.style.backgroundColor='#BDBDBD'
      last_day.style.backgroundColor='#BDBDBD'
      today.style.backgroundColor='#ff7043'
      filter_days.style.backgroundColor='#BDBDBD'
      this.getToday()
      this.GlobalRequiest(this.todayNight,this.today)
    },
    getFavoriotFilter() {
      
      this.getToday()
      this.GlobalRequiest(this.oldDate,this.newDate)
    },
    GlobalRequiest(x,y) {
      this.pulses=[];
      this.time=[];
      var token=localStorage.getItem('token');
      var url="http://api.smartsense.com.tr/api/doctor/getpatienthistorydaterange";
      var jsonObject={
          "patientId":localStorage.getItem("prodId"),
          "oldDate": x,
          "newDate": y
        }
        console.log(jsonObject);
        axios.post(url,jsonObject,{
        headers:{'Authorization': `Bearer ${token}`}
          }).then(res=> {
               if(res.data.code == "200") {
                 if(res.data.temperatures.length == 0) {
              // alert('hello')
              var getir=document.querySelector("#myChartPulsesss");
              getir.style.display="none";
              var nodatacardx=document.querySelector(".nodatacardthree");
              nodatacardx.style.display="block"
              
            }else {
              var getirx=document.querySelector("#myChartPulsesss");
              getirx.style.display="block";
              var nodatacard=document.querySelector(".nodatacardthree");
              nodatacard.style.display="none"
            }
          console.log(res.data);
          res.data.temperatures.forEach(element => {
          this.pulses.push(element.dataValue);
          this.time.push(element.saveDate.slice(11,19));
        });
          this.pageMode=1;
          this.updateChart();
               }if(res.data.code =="400") {
                 res.data.errors.forEach(res=> {
                   Vue.$toast.error(res);
                 })
               }
        }).catch(err=> {
          Vue.$toast.error(err);
        })
    },
    ChartCreate() {
      var ctx=document.getElementById('myChartPulsesss').getContext('2d');
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
              min: 33,
              max: 50,
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
            label:'°C (Sıcaklık)',
            backgroundColor:'transparent',
            borderColor:'rgb(255,112,67)',
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