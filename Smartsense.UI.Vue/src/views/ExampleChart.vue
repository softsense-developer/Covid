<template>
  <div class="container">
    <canvas id="myChart" width="600" height="200"></canvas>
  </div>
</template>

<script>
import axios from 'axios'
import Chart from 'chart.js';
export default {
    name:'ExampleChart',
    data(){
        return{
        myChart:'',
        time:[],
        oxygen:[]
        }
    },
    mounted(){

     this.getfavoriot();
    },
    methods:{
      getfavoriot(){
        var token=localStorage.getItem('token');
        var url="http://api.smartsense.com.tr/api/value/gethistory";
        axios.get(url,{
          headers:{'Authorization': `Bearer ${token}`}
          }).then(res=> {
            console.log(res.data);
            res.data.pulses.forEach(element => {
            this.pulses.push(element.dataValue);
            this.timepulses.push(element.saveDate.slice(0,10));
        });
        console.log(this.time);
        console.log(this.oxygen);
        this.chartx()
      })
    },
      chartx(){
            var ctx = document.getElementById('myChart').getContext('2d');
this.myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: this.time,
        datasets: [{
            label: 'Değer',
            data: this.oxygen,
            backgroundColor:"transparent",
            borderColor:"red",
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
        }
    }

}
</script>

<style>
#canvas{
    width: 100%;
}

</style>