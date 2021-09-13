<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">{{name}} {{surname}}Hastasına ait veriler</h1>
    <div class="container">
      <div class="row">
        <div class="col-sm-12 col-md-12 col-lg-12">
          <div class="card">
            <canvas id="myChartPulsess" width="800" height="300"></canvas>
          </div>
        </div>
        <div class="col-sm-12 col-md-12 col-lg-12">
          <div class="card">
            <canvas id="myChartPulses" width="800" height="300"></canvas>
          </div>
        </div>
        <div class="col-sm-12 col-md-12 col-lg-12">
          <div class="card">
            <canvas id="myChartPulsesım" width="800" height="300"></canvas>
          </div>
        </div>
      </div>
    </div>
    
   
    
  </div>
</template>

<script>
// import Vue from 'vue'
import axios from 'axios'
import Chart from 'chart.js';
import Navbar from '@/components/Navbarcompanian'
export default {
  name:"detailcompanian",
  components: {
    Navbar
  },
  data () {
    return {
      proId:this.$route.params.Pid,
      name:"",
      surname:"",
      myChart:"",
      charttemp:"",
      chartpulses:"",
      oxygen:[],
      oxygentime:[],
      pulses:[],
      pulsestime:[],
      temp:[],
      temptime:[]
    }
  },
  mounted() {
    this.getPatientData(this.proId);
    this.grafikCiz();
    this.ChartCreatePulses();
    this.ChartCreateTemp();
  },
  methods:  {
    getPatientData(id) {
      var token=localStorage.getItem('token');
      var url="http://api.smartsense.com.tr/api/companion/getpatientdata" +`/${id}`;
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        this.name=res.data.name;
        this.surname=res.data.surname;
        res.data.oxygen.forEach(element => {
          this.oxygen.push(element.dataValue);
          this.oxygentime.push(element.saveDate.slice(0,10))
        });
        res.data.pulses.forEach(element => {
          this.pulses.push(element.dataValue);
          this.pulsestime.push(element.saveDate.slice(0,10))
        });
        res.data.temperatures.forEach(element=> {
          this.temp.push(element.dataValue);
          this.temptime.push(element.saveDate.slice(0,10))
        })
        this.updateChart();
        this.updateChartPulses();
        this.updateChartTemp();
      })
    },
     ChartCreateTemp() {
      var ctx=document.getElementById('myChartPulsesım').getContext('2d');
      this.charttemp=new Chart(ctx, {
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
    updateChartTemp() {
      this.charttemp.data.datasets[0].data = this.temp;
      this.charttemp.data.labels = this.temptime;
      this.charttemp.update();
    },
    ChartCreatePulses() {
      var ctx=document.getElementById('myChartPulses').getContext('2d');
      this.chartpulses=new Chart(ctx, {
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
    updateChartPulses() {
      this.chartpulses.data.datasets[0].data = this.pulses;
      this.chartpulses.data.labels = this.pulsestime;
      this.chartpulses.update();
    },
    grafikCiz () {
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
      this.chart.data.datasets[0].data = this.oxygen;
      this.chart.data.labels = this.oxygentime;
      this.chart.update();
    }
  }
}
</script>

<style>

</style>