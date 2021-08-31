import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '../views/Home.vue'
import Projects from '../views/Projects.vue'
import Patientdetail from '../views/Patientdetail.vue'
import Patienthome from '../views/Patienthome.vue'
import Patientdoctoradd from '../views/Patientdoctoradd.vue'
import Changepasspatient from '../views/Changepasspatient.vue'
import Changepassdoctor from '../views/Changepassdoctor.vue'
import Putinfo from '../views/Putinfo.vue'
import Putinfodoctor from '../views/Putinfodoctor.vue'
import Companians from '../views/Companians.vue'
import Doctorhome from '../views/Doctorhome.vue'
import Doctorwarnings from '../views/Doctorwarnings.vue'
import Doctorrequiests from '../views/Doctorrequiests.vue'
import Supervisorhome from '../views/Supervisorhome.vue'
import Supervisordoctors from '../views/Supervisordoctors.vue'
import Patientrequiests from '../views/Patientrequiests.vue'
import Detailsupervisor from '../views/Detailsupervisor.vue'
import hospitalselect from '../views/Hospitalselect'
import Adminusers from '../views/Adminusers.vue'
import Detail from '../views/Detail.vue'
import deneme from '../views/deneme.vue'
import Home from '../views/Home.vue'
import Supervisorhospital from '../views/Supervisorhospital.vue'
import Supervisormaps from '../views/Supervisormaps.vue'
import Companianmaps from '../views/Companianmaps.vue'
import Companianhome from '../views/Companianhome.vue'
import Companianwarnings from '../views/Companianwarnings.vue'
import Detailcompanian from '../views/Detailcompanian.vue'
import Resetpassword from '../views/Resetpassword.vue'
import VerifyEmail from '../views/Verifymail.vue'
import Doctormaps from '../views/Doctormaps.vue'
import Adminhome from '../views/Adminhome.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Dashboard
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
  },
  {
    path: '/projects',
    name: 'projects',
    component: Projects
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    //component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/patienthome',
    name: 'patienthome',
    component: Patienthome,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 1) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/patientdetail',
    name: 'patientdetail',
    component: Patientdetail,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 1) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/doctorselect',
    name: 'patientdoctoradd',
    component: Patientdoctoradd,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 1) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/changepass',
    name: 'changepasspatient',
    component: Changepasspatient,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 1) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/changepassdoctor',
    name: 'changepassdoctor',
    component: Changepassdoctor,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 2) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/putprofile',
    name: 'putprofilechangepatient',
    component: Putinfo,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 1) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/putprofiledoctor',
    name: 'putprofiledoctor',
    component: Putinfodoctor,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 2) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/companians',
    name: 'companians',
    component: Companians,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 1) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/doctorhome',
    name: 'doctorhome',
    component: Doctorhome,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 2) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/doctorwarnings',
    name: 'doctorwarnings',
    component: Doctorwarnings,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 2) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/doctorrequiests',
    name: 'doctorrequiests',
    component: Doctorrequiests,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 2) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/detail/:Pid',
    name: 'detail',
    component: Detail,
    beforeEnter(to,from,next) {
      console.log(localStorage.getItem('token'));
      if(localStorage.getItem('token') && localStorage.getItem('role') == 2) {
        console.log("tokenOk");
        next();
      } 
      else {
        next("/");
      }
    }
  },
  {
    path: '/detailsupervisor/:Pid',
    name: 'detailsupervisor',
    component: Detailsupervisor,
  },
  {
    path: '/supervisorhome',
    name: 'supervisorhome',
    component: Supervisorhome,
  },
  {
    path: '/supervisorhospital',
    name: 'supervisorhospital',
    component: Supervisorhospital,
  },
  {
    path: '/supervisordoctors',
    name: 'supervisordoctors',
    component: Supervisordoctors,
  },
  {
    path: '/patientrequiests',
    name: 'patientrequiests',
    component: Patientrequiests,
  },
  {
    path: '/adminhome',
    name: 'adminhome',
    component: Adminhome,
  },
  {
    path: '/adminusers',
    name: 'adminusers',
    component: Adminusers,
  },
  {
    path: '/selecthospital/:Pid/:name',
    name: 'hospitalselect',
    component: hospitalselect,
  },
  {
    path: '/companianhome',
    name: 'companianhome',
    component: Companianhome,
  },
  {
    path: '/companianwarnings',
    name: 'companianwarnings',
    component: Companianwarnings,
  },
  {
    path: '/detailcompanian/:Pid',
    name: 'detailcompanian',
    component: Detailcompanian,
  },
  {
    path: '/doctormaps',
    name: 'doctormaps',
    component: Doctormaps,
  },
  {
    path: '/supervisormaps',
    name: 'supervisormaps',
    component: Supervisormaps,
  },
  {
    path: '/companianmaps',
    name: 'companianmaps',
    component: Companianmaps,
  },
  {
    path: '/deneme',
    name: 'deneme',
    component: deneme,
  },
  {
    path: '/resetpassword/:guid',
    name: 'resetpassword',
    component: Resetpassword,
  },
  {
    path:"/VerifyEmail/:guid", 
    name:'VerifyEmail',
    component: VerifyEmail,
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
