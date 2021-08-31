<template>
   <nav >
       <v-app-bar  color="green" dark app >
           <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
           <v-toolbar-title class="text-uppercase ">
               <span class="font-weight-light">Smart</span>
               <span>SenSe</span>
           </v-toolbar-title>
           <v-spacer></v-spacer>
           <v-menu offset-y>
      <template v-slot:activator="{ on }">
        <v-btn
          text
          v-on="on"
        >
          <v-icon left>expand_more</v-icon>
            <span>Menu</span>
        </v-btn>
      </template>
      <v-list flat>
        <v-list-item v-for="link in links"  :key="link.text" router :to="link.route" active-class="border">
          <v-list-item-title >{{link.text}}</v-list-item-title>
        </v-list-item>
      </v-list>
            </v-menu>
            <v-btn text @click="backlogin()">
                <span>Exit</span>
                <v-icon right>exit_to_app</v-icon>
             </v-btn>
       </v-app-bar>
      <v-navigation-drawer  v-model="drawer" dark app class="green darken-4">
          <v-layout column align-center>
               <v-flex class="mt-5"> 
                    <!-- <v-avatar size="100">
                            <img src="/img1.png" alt="">
                    </v-avatar> -->
                    <!-- <p class="white--text subheading mt-1 text-center">Hasta yakını</p> -->
                    <p class="white--text subheading mt-1 text-center"> {{name}} {{surname}}</p>
                    
               </v-flex>
               <!-- <v-flex class="mt-4 mb-4">
                <Popup />
               </v-flex> -->
          </v-layout>
          <v-list flat class="mt-5">
              <v-list-item v-for="link in links"  :key="link.text" router :to="link.route" active-class="border">
                  <v-list-item-action>
                     <v-icon >{{link.icon}}</v-icon>
                  </v-list-item-action>
                  <v-list-item-content >
                      <v-list-item-title >{{link.text}}</v-list-item-title>
                  </v-list-item-content>
              </v-list-item>
          </v-list>
      </v-navigation-drawer>
   </nav>
</template>
<script>
// import Popup from './Popup.vue'
export default {
   data: () => ({
      drawer: false,
      name:"",
      surname:"",
      userId:null,
      links :[
          {icon: 'fas fa-home', text:'Anasayfa', route: '/companianhome'},
          {icon: 'fas fa-home', text:'Uyarılar', route: '/companianwarnings'},
          {icon: 'fas fa-map', text:'Harita', route: '/companianmaps'},
        //   {icon: 'fas fa-bell', text:'Uyarılar', route: '/doctorwarnings'},
        //   {icon: 'fas fa-exclamation-circle', text:'İstekler', route: '/doctorrequiests'},
        //   {icon: 'folder', text:'Hasta Detayı', route: '/patientdetail'},
        //   {icon: 'person', text:'Doktor seçim', route: '/doctorselect'},
        //    {icon: 'person', text:'Hesap', route: '/changepass'},
        //    {icon: 'person', text:'Refakatçılar', route: '/companians'}
      ]
     
    }),
    mounted () {
      this.pageMode=localStorage.getItem('pageMode');
      this.tryMedia();
      this.tryMediatwo();
      this.name=localStorage.getItem("namex");
      this.surname=localStorage.getItem("surnamex");
      // this.userId=localStorage.getItem("userId");
    },
    methods: {
      tryMedia() {
      const mediaQuery = window.matchMedia('(max-width: 768px)')
      // Check if the media query is true
      if (mediaQuery.matches) {
        console.log('merhaba dünya');
        this.drawer=false
      }
    },
    tryMediatwo() {
      const mediaQuery = window.matchMedia('(min-width: 768px)')
      // Check if the media query is true
      if (mediaQuery.matches) {
        console.log('merhaba dünya');
        this.drawer=true
      }
    },
      backlogin() {
      localStorage.clear();
      this.$router.push('/');
      },
      
    },
    
    components: {
    // Popup
  },

   
}
</script>
<style scoped>
.border {
  border-left: 4px solid #0ba518;
}
</style>
