<template>
  <div id="app">
     <router-view></router-view>
  </div>
</template>

<script>

// import LineChart from './components/LineChart.vue'
export default {
  name: 'App',
  components: {
    // LineChart
    
  },
}
</script>

<style>

*{
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
}
 #app{
  height: 100vh;
  width: 100%;
  background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));
} 
</style>
