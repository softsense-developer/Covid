<template>
  <v-app>
  <!-- <Navbar/> -->
    <v-content class="ma-4">
      <router-view></router-view>
    </v-content>
     
  </v-app>
 
</template>

<script>

// import Navbar from '@/components/Navbar'

export default {
  name: 'App',

  components: {
  //  Navbar,

  },
  mounted () {
    
  },

  data: () => ({
    pageMode:0
  }),
};
</script>
