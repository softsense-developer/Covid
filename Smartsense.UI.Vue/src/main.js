import Vue from 'vue'
import VueRouter from "vue-router"
import App from './App.vue'
import Login from './views/Login'
import HomePagePatient from './views/HomePagePatient'
import HomePageDoctor from './views/HomePageDoctor'
import Location from './views/PatientLocation'
import Profile from './views/Profile'
import {store} from './store/store'
import Account from './views/Account'
import About from './views/About'
import Islem from './views/Islem'
import addgoogle from './views/DoctorLocations'
import selectdoctor from './views/SelectDoctor'
// import about from './views/About'
import Vuelidate from 'vuelidate';
import * as VueGoogleMaps from "vue2-google-maps"
import VueSidebarMenu from 'vue-sidebar-menu'
import 'vue-sidebar-menu/dist/vue-sidebar-menu.css'
 import details from './views/Details'
 import VerifyEmail from './views/VerifyEmail'
 import warnings from './views/Warnings'
 import infoUpdate from './views/infoUpdate'
 import ResetPassword from './views/ResetPassword'
 import Notification from './views/Notification'
 import AdminHome from './views/Admin'
 import Hospitals from './views/Hospitals'
 import HospitalAdd from './views/HospitalAdd'
 import hospitalselect from './views/HospitalSelect'
 import HomeSupervisor from './views/HomeSupervisor'
 import SupHospitals from './views/SupHospitals'
 import SupDoctors from './views/SupDoctors'
 import SupLocations from './views/SupLocations'
 import SupDoctorPromotion from './views/SupDoctorPromotion'
 import ExampleChart from './views/ExampleChart'
 import SupDetails from './views/SupDetails'
 import CompanianAdd from './views/CompanianAdd'
 import CompanianHome from './views/CompanianHome'
 import CompMaps from './views/CompMaps'
 import CompanianWarnings from './views/CompanianWarnings'
 import PatientNotification from './views/PatientNotification'
 import CompList from './views/CompanianList'
Vue.use(VueSidebarMenu)

Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBD9pknSdrRqlznNiaQG0YahqlEcEMPFL8",
    libraries: "places" // necessary for places input
  }
});


Vue.use(Vuelidate);

Vue.config.productionTip = false
Vue.use(VueRouter)


export const router=new VueRouter({
  routes :[
    {
      path: '/',
      name: 'About',
      component: Login,
       beforeEnter(to,from,next){
          console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/SupDetails/:Pid',
      name: 'SupDetails',
      component: SupDetails,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==4){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/CompList',
      name: 'CompList',
      component: CompList,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==1){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/PatientNotification',
      name: 'PatientNotification',
      component: PatientNotification,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==1){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    
    {
      path: '/CompAdd',
      name: 'CompAdd',
      component: CompanianAdd,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==1){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/CompanianWarnings',
      name: 'CompWarnings',
      component: CompanianWarnings,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==5){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/CompMaps',
      name: 'CompMaps',
      component: CompMaps,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==5){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/CompHome',
      name: 'CompAdd',
      component: CompanianHome,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==5){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/DoctorPromotion", 
      name:'DoctorPromotion',
      component: SupDoctorPromotion,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')&&localStorage.getItem('roleId')==3){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/Chart", 
      name:'DoctorPromotion',
      component: ExampleChart,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==4){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/Homepage", 
      name:'Homepage',
      component: HomePagePatient,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==1){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/HomeSupervisor", 
      name:'Homesupervisor',
      component: HomeSupervisor,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==3){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/SupervisorLocations", 
      name:'SupLocations',
      component: SupLocations,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==3){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/AllHospitals", 
      name:'SupHospitals',
      component: SupHospitals,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==3){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/AllDoctors", 
      name:'SupDoctors',
      component: SupDoctors,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==3){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/AdminHome", 
      name:'Homepage',
      component: AdminHome,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==4){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/Hospitals", 
      name:'Hospitals',
      component: Hospitals,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==4){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/HospitalAdd", 
      name:'HospitalAdd',
      component: HospitalAdd,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==4){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/Notification", 
      name:'Notification',
      component: Notification,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/VerifyEmail/:guid", 
      name:'VerifyEmail',
      component: VerifyEmail,
    },
    {
      path:"/ResetPassword/:guid", 
      name:'ResetPassword',
      component: ResetPassword,
    },
    {
      path:"/Warnings", 
      name:'Warnings',
      component: warnings,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==2){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/SelectDoctor", 
      name:'SelectDoctor',
      component: selectdoctor,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==1){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/details/:Pid',
      name: 'details',
      component: details,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')&&localStorage.getItem('roleId')==5 ||localStorage.getItem('roleId')==2 ||localStorage.getItem('roleId')==3){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path: '/selecthospital/:Pid/:name',
      name: 'hospitalselect',
      component: hospitalselect,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')&&localStorage.getItem('roleId')==4){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/Account", 
      name:'Account',
      component: Account,
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       }
    },
    {
      path:"/Location", 
      component: Location,  
    },
    {
      path:"/Locations", 
      component: addgoogle, 
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==2){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       } 
    },
    // {
    //   path: '/details/:Pid',
    //   name: 'details',
    //   component: details,
    //   beforeEnter(to,from,next){
    //     //  console.log(localStorage.getItem('token'));
    //      if(localStorage.getItem('token')){
    //       console.log("tokenOk");
    //        next();
    //      }else{
    //        next("/Login");
    //      }
    //    } 
    // },
    {
      path:"/Login", 
      component: Login,  
    },
    {
      path:"/About", 
      component: About, 
      children:[
        {path:'',component:Profile},
        {path:'/About/Update',component:Islem}
      ],
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       },
    },
    {
      path:"/DoktorHome", 
      component: HomePageDoctor, 
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token') && localStorage.getItem('roleId')==2){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       } 
    },
    {
      path:"/Profile", 
      component: Profile, 
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       } 
    },
    {
      path:"/infoUpdate", 
      component: infoUpdate, 
      beforeEnter(to,from,next){
        //  console.log(localStorage.getItem('token'));
         if(localStorage.getItem('token')){
          console.log("tokenOk");
           next();
         }else{
           next("/Login");
         }
       } 
    },
    
     
  ],
  mode:"history",
  scrollBehavior: function (to) {
    if (to.hash) {
      return {
        selector: to.hash
      }
    }
  },
});



new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')
