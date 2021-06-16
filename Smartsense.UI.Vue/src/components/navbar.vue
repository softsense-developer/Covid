<template>
<div >
  <!-- merhaba git -->
  <nav id="sidebar" class="sidebar-wrapper">
    <div class="sidebar-content">
      <div class="sidebar-brand">
        <a href="#">Smartsense</a>
        <div id="close-sidebar" @click="sidebarClose()">
          <i class="fas fa-times"></i>
        </div>
        
      </div>
      <div class="sidebar-header">
        <div class="user-pic">
          <img class="img-responsive img-rounded" src="https://raw.githubusercontent.com/azouaoui-med/pro-sidebar-template/gh-pages/src/img/user.jpg"
            alt="User picture">
        </div>
        <div class="user-info">
          <span class="user-name">{{isim}}
            <strong>{{surname}}</strong>
          </span>
          <span class="user-role" v-if="roleId==2"><span style="color:white">Doktor  ID:{{userID}}</span></span>
          <span class="user-role" v-if="roleId==1"><span style="color:white">Hasta  ID:{{userID}}</span></span>
          <span class="user-role" v-if="roleId==4"><span style="color:white">Admin  ID:{{userID}}</span></span>
          <span class="user-role" v-if="roleId==3"><span style="color:white">Supervisor  ID:{{userID}}</span></span>
          <span class="user-role" v-if="roleId==5"><span style="color:white">Companian  ID:{{userID}}</span></span>
          <!-- <span class="user-status">
            <i class="fa fa-circle"></i>
            <span>Online</span>
          </span> -->
        </div>
      </div>
      <!-- sidebar-header  -->
      <!-- sidebar-search  -->
      <div class="sidebar-menu">
        <ul>
          <li class="header-menu">
            <span>İçerik</span>
          </li>
          <li>
            <router-link to="/DoktorHome" tag="a" style="cursor:pointer;" v-if="roleId==2">
              <i class="fas fa-home"></i>
              <span>Anasayfa</span>
              <!-- <span class="badge badge-pill badge-warning">Yeni</span> -->
            </router-link>
            <router-link to="/HomePage" tag="a" style="cursor:pointer;" v-if="roleId==1">
              <i class="fas fa-home"></i>
              <span>Anasayfa</span>
              <!-- <span class="badge badge-pill badge-warning">Yeni</span> -->
            </router-link>
            <router-link to="/AdminHome" tag="a" style="cursor:pointer;" v-if="roleId==4">
              <i class="fas fa-home"></i>
              <span>Anasayfa</span>
              <!-- <span class="badge badge-pill badge-warning">Yeni</span> -->
            </router-link>
            <router-link to="/HomeSupervisor" tag="a" style="cursor:pointer;" v-if="roleId==3">
              <i class="fas fa-home"></i>
              <span>Anasayfa</span>
              <!-- <span class="badge badge-pill badge-warning">Yeni</span> -->
            </router-link>
            <router-link to="/CompHome" tag="a" style="cursor:pointer;" v-if="roleId==5">
              <i class="fas fa-home"></i>
              <span>Anasayfa</span>
              <!-- <span class="badge badge-pill badge-warning">Yeni</span> -->
            </router-link>
          </li>
          <li>
            <router-link to="/Warnings" tag="a" v-if="roleId==2">
              <i class="fas fa-exclamation-circle"></i>
              <span>Uyarılar</span>
            </router-link>
            <router-link to="/CompanianWarnings" tag="a" v-if="roleId==5">
              <i class="fas fa-exclamation-circle"></i>
              <span>Uyarılar</span>
            </router-link>
            <router-link to="/Hospitals" tag="a" v-if="roleId==4">
              <i class="fas fa-h-square"></i>
              <span>Hastaneler</span>
            </router-link>
            <router-link to="/AllHospitals" tag="a" v-if="roleId==3">
              <i class="fas fa-h-square"></i>
              <span>Hastane</span>
            </router-link>
         </li>
          <li>
            <router-link to="/Profile" tag="a" v-if="roleId==1">
              <i class="fas fa-user"></i>
              <span>Hasta Profili</span>
            </router-link>
             <router-link to="/HospitalAdd" tag="a" v-if="roleId==4">
              <i class="fas fa-hospital-user"></i>
              <span>Hastane Ekle</span>
            </router-link>
            <router-link to="/AllDoctors" tag="a" v-if="roleId==3">
              <i class="fas fa-hospital-user"></i>
              <span>Doktorlar</span>
            </router-link>
          </li>
           <li>
            <router-link to="/SelectDoctor" tag="a" v-if="roleId==1">
              <i class="fas fa-user"></i>
              <span>Doktor Seçimi</span>
            </router-link>
          </li>
           <li class="sidebar-dropdown" style="cursor:pointer;" v-if="roleId==1 || roleId==2 || roleId==3 || roleId==4 || roleId==5" >
            <a  @click="open()">
              <i class="fa fa-chart-line" ></i>
              <span>Hesap Ayarları</span>
              <!-- <span class="badge badge-pill badge-success">2</span> -->
            </a>
            <div class="sidebar-submenu" >
              <ul>
                <li>
                  <router-link to="/infoUpdate" tag="a">Bilgilerimi Güncelle
                  </router-link>
                </li>
                
                <li>
                  <router-link to="/Account" tag="a">Şifremi Güncelle
                  </router-link>
                </li>
              </ul>
            </div>
          </li>
          <li class="sidebar-dropdown" style="cursor:pointer;" v-if="roleId==1" >
            <a @click="openx()">
              <i class="fa fa-chart-line" ></i>
              <span>Refakatçi İşlemleri</span>
              <!-- <span class="badge badge-pill badge-success">2</span> -->
            </a>
            <div class="sidebar-submenux" >
              <ul>
                <li>
                  <router-link v-if="roleId==1" to="/CompAdd" tag="a">Refakatçi Ekle
                  </router-link>
                </li>
                 <li>
                  <router-link v-if="roleId==1" to="/CompList" tag="a">Refakatçılarım
                  </router-link>
                </li>
                <!-- <li>
                  <router-link v-if="roleId==1" to="/CompAdd" tag="a">Refakatçi Ekle
                  </router-link>
                </li> --> 
              </ul>
            </div>
          </li>
          <li>
            <router-link to="/Locations" tag="a" v-if="roleId==2" >
              <i class="fa fa-globe"></i>
              <span>Konum</span>
            </router-link>
            <router-link to="/Location" tag="a" v-if="roleId==1" >
              <i class="fa fa-globe"></i>
              <span>Konum</span>
            </router-link>
            <router-link to="/SupervisorLocations" tag="a" v-if="roleId==3" >
              <i class="fa fa-globe"></i>
              <span>Konum</span>
            </router-link>
            <router-link to="/CompMaps" tag="a" v-if="roleId==5" >
              <i class="fa fa-globe"></i>
              <span>Konum</span>
            </router-link>
          </li>
          <li >
            <router-link to="/DoctorPromotion" tag="a" v-if="roleId==3" >
              <i class="fa fa-globe"></i>
              <span>Doktor Ata</span>
            </router-link>
          </li>
          <!-- <li class="header-menu" v-if="roleId==1 || roleId==2">
            <span>Extra</span>
          </li> -->
          <!-- <li v-if="roleId==1 || roleId==2">
            <a href="#">
              <i class="fa fa-book"></i>
              <span>İletişim</span>
              <span class="badge badge-pill badge-primary">Beta</span>
            </a>
          </li> -->
       </ul>
      </div>
      <!-- sidebar-menu  -->
    </div>
    <!-- sidebar-content  -->
    <div class="sidebar-footer">
      <!-- <a href="#">
        <i class="fa fa-bell"></i>
        <span class="badge badge-pill badge-warning notification">3</span>
      </a> -->
      <router-link to="/Notification" tag="a" v-if="roleId==2 || roleId==5">
        <i class="fa fa-envelope"></i>
        <span class="badge badge-pill badge-success notification">{{doctors.length}}</span>
      </router-link>
      <router-link to="/PatientNotification" tag="a" v-if="roleId==1">
        <i class="fa fa-envelope"></i>
        <span class="badge badge-pill badge-success notification">{{patients.length}}</span>
      </router-link>
      <router-link to="/infoUpdate" tag="a" href="#">
        <i class="fa fa-cog"></i>
        <span class="badge-sonar"></span>
      </router-link>
      <router-link href="#" to="/Login" tag="a" @click="exit()">
        <i class="fa fa-power-off" @click="exit()"></i>
      </router-link>
    </div>
  </nav>
  <!-- sidebar-wrapper  -->
   
  <!-- page-content" -->
</div>
 
</template>

<script>

import axios from 'axios'
export default {
name:'Content',
components:{

},

  data() {
    return {
      isim:"",
      surname:"",
      veriler:[],
      roleId:null,
      doctors:[],
      userId:"",
      patients:[]
    }
  },

  mounted() {
    this.isim=this.getName();
    console.log(this.isim);
    this.surname=this.getSurname();
    // this.getChart();
    this.getRoleId();
    this.getpatientRequiest();
    this.tryMedia(); 
    this.userID=this.getuserID();
    this.getpatientRequiestPatient();
    window.addEventListener('resize', this.onResize)
  },

  methods: {
    onResize() {
      const mediaQuery = window.matchMedia('(max-width: 768px)')
        // Check if the media query is true
        if (mediaQuery.matches) {
        // Then trigger an alert
          var close=document.querySelector('#close-sidebar');
          close.click();
        }
    },


    tryMedia() {
      const mediaQuery = window.matchMedia('(max-width: 768px)')
      // Check if the media query is true
      if (mediaQuery.matches) {
      // Then trigger an alert
        var close=document.querySelector('#close-sidebar');
        close.click();
      }
    },

    getuserID() {
      var getirUserID=localStorage.getItem('userId');
      return getirUserID;
    },

    open() {
      var y=document.querySelector('.sidebar-wrapper .sidebar-menu .sidebar-submenu');
        if(y.classList.contains("k")===true) {
          y.classList.remove("k");
        } else{
        y.classList.add("k");
      }
    },
    openx() {
      var y=document.querySelector('.sidebar-wrapper .sidebar-menu .sidebar-submenux');
        if(y.classList.contains("k")===true) {
          y.classList.remove("k");
        } else{
        y.classList.add("k");
      }
    },

    sidebarClose() {
      var closeSide=document.querySelector('.page-wrapper');
      closeSide.classList.remove('toggled')
    },

    sidebarOpen() {
      var openSide=document.querySelector('.page-wrapper');
      openSide.classList.add('toggled')
    }, 

    getRoleId() {
      var roleId=localStorage.getItem('roleId');
      console.log(roleId+'xxxxxxxxx');
      this.roleId=roleId;
    },

    getName() {
      var getirName=localStorage.getItem('name');
      return getirName;
    },

    getSurname() {
      var getirSurname=localStorage.getItem('surname');
      return getirSurname;
    },

    exit(){
      localStorage.clear();
      this.$router.push('/Login');
    }, 

    getpatientRequiest() {
      var url=`http://api.smartsense.com.tr/api/doctor/GetConnections`
      var token=localStorage.getItem('token');
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
          console.log(res);
          res.data.connectionRequests.forEach(res=>{
          this.doctors.push(res);
        })
      console.log('length'); 
     })
    },
    getpatientRequiestPatient() {
      var url=`http://api.smartsense.com.tr/api/patient/getpromotions`
      var token=localStorage.getItem('token');
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
          console.log(res);
          res.data.connectionRequests.forEach(res=>{
          this.patients.push(res);
        })
      console.log('length'); 
     })
    }
  },
    beforeDestroy() {
      // Unregister the event listener before destroying this Vue instance
      window.removeEventListener('resize', this.onResize)
    }
}

</script>

<style scoped>
@import '../assets/css/content.css'; 
@import '../assets/css/login.css'; 

</style>