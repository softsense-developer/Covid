import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';
import Vuelidate from 'vuelidate'
import VueToast from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-sugar.css';
import * as VueGoogleMaps from "vue2-google-maps"
Vue.use(VueToast);
Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBD9pknSdrRqlznNiaQG0YahqlEcEMPFL8",
    libraries: "places" // necessary for places input
  }
});
Vue.config.productionTip = false
Vue.use(Vuelidate)
new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
