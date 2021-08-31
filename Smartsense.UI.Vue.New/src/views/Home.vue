<template>
  <div class="home">
    <!-- <h1>{{pagemode}}</h1> -->
    
    <Forgotpass @pagemodewasedited="pagemode=$event" :pagemode="pagemode" v-if="pagemode==7"/>
    <Login @pagemodewasedited="pagemode=$event" v-if="pagemode==3 || pagemode==0"/>
    <Register @pagemodewasedited="pagemode=$event" :pagemode="pagemode"/>
  </div>
</template>

<script>
// @ is an alias to /src
// import Sidebar from './components/Sidebar.vue'
import Forgotpass from '@/components/Forgotpass.vue'
import Login from '@/components/Login.vue'
import Register from '@/components/Register.vue'

export default {
  name: 'Home',
  components: {
    Login,
    Register,
    Forgotpass
  },

  data() {
    return {
      pagemode:3,
    }
  }
}
</script>
<style scoped>
  @import '../assets/css/login.css';
</style>
