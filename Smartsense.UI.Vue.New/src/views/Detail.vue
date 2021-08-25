<template>
  <div>  
     <Navbar/>
     <h1 class="subheading grey--text">Hastaya ait veriler</h1>
      <div class="container" >
        <div class="row">
          <div class="col-sm-4 col-md-4 col-lg-4">
            <div class="small-box bg-success text-white text-center">
                  <div class="inner">
                    <h3>{{lastValueOxygen.dataValue}}</h3>
                    <p>Son spo2 verisi</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-bag"></i>
                  </div>
                  <!-- <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a> -->
                </div>
          </div>
          <div class="col-sm-4 col-md-4 col-lg-4">
            <div class="small-box bg-danger text-white text-center">
                  <div class="inner">
                    <h3>{{lastValuePulses.dataValue}}</h3>
                    <p>Son nabız verisi</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-bag"></i>
                  </div>
                  <!-- <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a> -->
                </div>
          </div>
          <div class="col-sm-4 col-md-4 col-lg-4">
            <div class="small-box  text-white text-center" style="background:#fd7e14">
                  <div class="inner">
                    <h3>{{lastValueTemp.dataValue}}</h3>
                    <p>Son sıcaklık verisi</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-bag"></i>
                  </div>
                  <!-- <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a> -->
                </div>
          </div>
        </div>
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
import Navbar from '@/components/Navbardoctor'
// import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
import LineChart from '../components/LineChartDoctor.vue'
import LineChartPulses from '../components/LineChartPulsesDoctor.vue'
import LineChartTemp from '../components/LineChartTempDoctor.vue'
export default {
  name:"mainSide",
  data() {
    return {
      proId:this.$route.params.Pid,
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
    this.getAllSeizures(this.proId)
    // this.getfavoriot();
    // this.getfavoriotPulses();
    // this.getfavoriotTemp();
    // setTimeout(() => this.pageMode=1, 2000);
  },
  methods: {
    getAllSeizures(id) {
      var token=localStorage.getItem('token');
      var url="http://api.smartsense.com.tr/api/doctor/getpatientdata" +`/${id}`;
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        if(res.data.code == "200") {
          res.data.oxygen.forEach(element => {
            this.oxygen.push(element);
          });
          if(this.oxygen.length == 0) {
            var jsonobject={};
            jsonobject.dataValue="son veriniz bulumamaktadır"
            this.lastValueOxygen=jsonobject.dataValue;
          } else {
            this.lastValueOxygen=this.oxygen.pop();
          }  
        res.data.pulses.forEach(element => {
            this.pulses.push(element);
          });
          if(this.pulses.length == 0) {
            var jsonobjectpulses={};
            jsonobject.dataValue="son veriniz bulumamaktadır"
            this.lastValuePulses=jsonobjectpulses.dataValue;
          } else {
            this.lastValuePulses=this.pulses.pop();
          }    
        res.data.temperatures.forEach(element => {
            this.temp.push(element);
          });
          if(this.temp.length == 0) {
            var jsonobjecttemp={};
            jsonobject.dataValue="son veriniz bulumamaktadır"
            this.lastTimeTemp=jsonobjecttemp.dataValue;
          } else {
            this.lastValueTemp=this.temp.pop()
          }
        }
      })
    }

    
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
  .small-box {
  border-radius: 0.25rem;
  box-shadow: 0 0 1px rgba(0, 0, 0, 0.125), 0 1px 3px rgba(0, 0, 0, 0.2);
  display: block;
  margin-bottom: 20px;
  position: relative;
}

.small-box > .inner {
  padding: 10px;
}

.small-box > .small-box-footer {
  background-color: rgba(0, 0, 0, 0.1);
  color: rgba(255, 255, 255, 0.8);
  display: block;
  padding: 3px 0;
  position: relative;
  text-align: center;
  text-decoration: none;
  z-index: 10;
}

.small-box > .small-box-footer:hover {
  background-color: rgba(0, 0, 0, 0.15);
  color: #fff;
}

.small-box h3 {
  font-size: 2.2rem;
  font-weight: 700;
  margin: 0 0 10px;
  padding: 0;
  white-space: nowrap;
}

@media (min-width: 992px) {
  .col-xl-2 .small-box h3,
  .col-lg-2 .small-box h3,
  .col-md-2 .small-box h3 {
    font-size: 1.6rem;
  }
  .col-xl-3 .small-box h3,
  .col-lg-3 .small-box h3,
  .col-md-3 .small-box h3 {
    font-size: 1.6rem;
  }
}

@media (min-width: 1200px) {
  .col-xl-2 .small-box h3,
  .col-lg-2 .small-box h3,
  .col-md-2 .small-box h3 {
    font-size: 2.2rem;
  }
  .col-xl-3 .small-box h3,
  .col-lg-3 .small-box h3,
  .col-md-3 .small-box h3 {
    font-size: 2.2rem;
  }
}

.small-box p {
  font-size: 1rem;
}

.small-box p > small {
  color: #f8f9fa;
  display: block;
  font-size: .9rem;
  margin-top: 5px;
}

.small-box h3,
.small-box p {
  z-index: 5;
}

.small-box .icon {
  color: rgba(0, 0, 0, 0.15);
  z-index: 0;
}

.small-box .icon > i {
  font-size: 90px;
  position: absolute;
  right: 15px;
  top: 15px;
  transition: -webkit-transform 0.3s linear;
  transition: transform 0.3s linear;
  transition: transform 0.3s linear, -webkit-transform 0.3s linear;
}

.small-box .icon > i.fa, .small-box .icon > i.fas, .small-box .icon > i.far, .small-box .icon > i.fab, .small-box .icon > i.fal, .small-box .icon > i.fad, .small-box .icon > i.ion {
  font-size: 70px;
  top: 20px;
}

.small-box .icon svg {
  font-size: 70px;
  position: absolute;
  right: 15px;
  top: 15px;
  transition: -webkit-transform 0.3s linear;
  transition: transform 0.3s linear;
  transition: transform 0.3s linear, -webkit-transform 0.3s linear;
}

.small-box:hover {
  text-decoration: none;
}

.small-box:hover .icon > i, .small-box:hover .icon > i.fa, .small-box:hover .icon > i.fas, .small-box:hover .icon > i.far, .small-box:hover .icon > i.fab, .small-box:hover .icon > i.fal, .small-box:hover .icon > i.fad, .small-box:hover .icon > i.ion {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}

.small-box:hover .icon > svg {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}

@media (max-width: 767.98px) {
  .small-box {
    text-align: center;
  }
  .small-box .icon {
    display: none;
  }
  .small-box p {
    font-size: 12px;
  }
  
}



</style>

