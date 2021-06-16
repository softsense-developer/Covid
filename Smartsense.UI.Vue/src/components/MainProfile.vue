<template>
  <div>
    <div id="snackbar">Some text some message..</div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
       <div class="container" >
         <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Hasta Profili</h5>
          </div>
        </div>
        <div class="d-flex text-center justify-content-center" v-if="display==0" style="margin-top:22%;">
          <scaling-squares-spinner class="text-info"
            :animation-duration="1000"
            :size="75"
            :color="'#80CBC4'"
          />
        </div>
      <div class="row d-flex justify-content-start rows" >
        <!-- <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid #607D8B">
            <h5 style="color:#607D8B;text-transform: capitalize;">Kişisel Bilgiler</h5>
          </div> -->
            <!-- .xa mobil kısım için dikkat et  -->
        <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5 xa" >
          <div class="account-update" v-if="pageMode==1">
            <div class="form-container">
              <div class="form-inner">
                <form  class="login">
                  <div class="field">
                    <input type="text" placeholder="TC Kimlik No" required>
                  </div>
                  <div class="field">
                    <input type="date" id="birthday" name="birthday" required>
                  </div>
                  <div class="field">
                      <!-- <label for="exampleFormControlSelect1">Cinsiyet Seçiniz</label> -->
                      <select class="form-control" id="exampleFormControlSelect1">
                      <option value="0">Erkek</option>
                      <option value="1">Kadın</option>
                      <option value="2">Belirtmek istemiyorum</option>
                      </select>
                  </div>
                  <div class="field">
                      <!-- <label for="exampleFormControlSelect1">Cinsiyet Seçiniz</label> -->
                      <select class="form-control" id="exampleFormControlSelect2">
                      <option value="0">Hastanede Karantinada</option>
                      <option value="1">Evde Karantinada</option>
                      <option value="3">Şüpheli</option>
                      <option value="4">Tahliye</option>
                      <option value="5">Diğer</option>
                      </select>
                  </div>
                    <div class="field">
                    <input type="text" placeholder="Teşhis"  required>
                  </div>
                  <!-- <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;">
                    <a class="backx"  style="cursor:pointer;color:green;" @click="pageModes()">Şifremi Güncelle</a> 
                  </div> -->
                  <div class="field btn" id="x">
                    <div class="btn-layer"></div>
                    <input value="Bilgilerimi Kaydet" type="button" @click="ProfilCreate()" >
                  </div>
                  <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;">
                    <!-- <a class="backx" ><i class="fas fa-arrow-left" style="font-size:20px;"></i> Geri Dön</a>  -->
                  </div>
                    <!-- <div class="signup-link" @click="go()" style="cursor:pointer;">
                    Hesabınız yok mu? Şimdi Kayıt Ol
                    </div> -->
                </form>
              </div>
            </div>
          </div>
              <div class="profile-cart" v-if="pageMode==0" :style="{opacity:opacity}" >
                <div class="container">
                  <div class="row">
                    <div class="card " style="width: 100%;" >
                      <div class="container">
                        <div class="row">
                          <div class="col-sm-6 col-md-6 col-lg-6">
                            <img class="card-img-top mt-5 imagex" src="../assets/user.png" alt="Card image cap" style="height:200px;">
                          </div>
                          <div class="col-sm-6 col-md-6 col-lg-6">
                            <div class="card-body dikkat">
                        <div class="d-flex flex-column">
                        <h5 class="card-title">Profil bilgileri</h5>
                        <p class="card-text">Kimlik Numarası: {{Tckn}}</p>
                        <p class="card-text">Doğum tarihi: {{bdate}}</p>
                        <p class="card-text">Teshis: {{teshis}}</p>
                        <p class="card-text">email bilgisi: {{mail}}</p>
                        <p class="card-text" v-if="gender==0">cinsiyet: erkek</p>
                        <p class="card-text" v-if="gender==1">cinsiyet: kadın</p>
                        <p class="card-text" v-if="gender==2">cinsiyet: belirtmekistemiyorum</p>
                        </div>
                      </div>
                          </div>
                        </div>
                      </div>
                      <div class="d-flex justify-content-end">
                          <a class="btn w-50 text-white" @click="profilUpdate()" style="background:rgb(28,134,122)">Go somewhere</a>
                        </div>
                    </div>
                  </div>
                </div>
                
                    
                  
              </div>
             
              <div class="account-update mb-5" v-if="pageMode==3">
                <div class="form-container">
                  <div class="form-inner">
                    <form  class="login">
                      <div class="field">
                        <input type="text" placeholder="TC Kimlik No" required >
                      </div>
                      <div class="field">
                        <input type="date" id="birthday" name="birthday" required>
                      </div>
                      <div class="field">
                          <select class="form-control" id="exampleFormControlSelect1">
                          <option value="0">Erkek</option>
                          <option value="1">Kadın</option>
                          <option value="2">Belirtmek istemiyorum</option>
                          </select>
                      </div>
                      <div class="field">
                        <select class="form-control" id="exampleFormControlSelect2">
                          <option value="0">Hastanede Karantinada</option>
                          <option value="1">Evde Karantinada</option>
                          <option value="3">Şüpheli</option>
                          <option value="4">Tahliye</option>
                          <option value="5">Diğer</option>
                        </select>
                      </div>
                       <div class="field">
                        <input type="text" placeholder="Teşhis"  required>
                      </div>
                       <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;">
                        <i class="fas fa-arrow-left"></i> <a class="backx"  style="cursor:pointer;color:green;" @click="geri()">Geri</a> 
                      </div> 
                      <div class="field btn" id="x">
                        <div class="btn-layer" style="background:background:rgb(33,140,127)"></div>
                        <input value="Güncelle" type="button" @click="Update()" >
                      </div>
                      <div class="pass-link mt-5 pass-link-two" style="border:1px solid white;width:35%;border-radius:5px;">
                        <!-- <a class="backx" ><i class="fas fa-arrow-left" style="font-size:20px;"></i> Geri Dön</a>  -->
                      </div>
                        <!-- <div class="signup-link" @click="go()" style="cursor:pointer;">
                        Hesabınız yok mu? Şimdi Kayıt Ol
                        </div> -->
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    </main>
      
  </div>
</template>

<script>
import {ScalingSquaresSpinner} from 'epic-spinners'
import axios from 'axios'
export default {
  name:"Account",
  components:{
       ScalingSquaresSpinner
  },

data(){
  return{
  mail:"",
  gender:"",
  Tckn:"",
  bdate:"",
  teshis:"",
  userStatus:"",
  oldPassword:"",
  newPassword:"",
  confirmPassword:"",
  pageMode:0,
  errors:[],
  roleId:"",
  opacity:0,
  background:'yellow',
  messages:'',
  display:1
  
    // roleId:""
  }
},

mounted(){
  this.getProfiles()
  this.GetroleId()
},

methods:{

  Toast(messages) {
  // Get the snackbar DIV
    var x = document.getElementById("snackbar");
    // Add the "show" class to DIV
    x.className = "show";
    x.style.backgroundColor='rgb(15,120,108)';
    x.innerHTML=`<p style='color:white;'>${messages}</p>`
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  },

  ToastError() {
  // Get the snackbar DIV
    var x = document.getElementById("snackbar");
    // Add the "show" class to DIV
    x.className = "show";
    x.innerHTML=`<p style='color:white;'>bilgilerinizi kontrol ediniz</p>`
    x.style.backgroundColor='#EF5350';
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  },

  Update(){
    var url='http://api.smartsense.com.tr/api/patient/putinfo'
    var token=localStorage.getItem('token');
    var selectkutu = document.getElementById('exampleFormControlSelect1');
    var selectkutu1 = document.getElementById('exampleFormControlSelect2');
    console.log()
		var selectkutu_value = selectkutu.options[selectkutu.selectedIndex].value;
    var selectkutu_value1 = selectkutu1.options[selectkutu1.selectedIndex].value;
    var JsonObjectsx={
      "dateOfBirth": this.bdate,
      "identityNumber": this.Tckn,
      "gender": Number(selectkutu_value),
      "userStatus": Number(selectkutu_value1),
      "diagnosis": this.teshis
    }
    console.log(JsonObjectsx);
    axios.put(url,JsonObjectsx,{
        headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
        console.log(res);
        // this.returnLogin()
        if(res.data.message!==null && res.data.code==200 ){
          this.messages=res.data.message;
          console.log(this.messages);
          this.Toast(this.messages); //taost function tetiklendi
          this.display=0;
          this.pageClear();
          setTimeout(() => {
            this.geri(),this.display=1
          }, 2000);
        }else{
          this.messages=res.data.errors[0];
          this.ToastError();
        }
        // this.Toast();
        
    })
  },

  geri(){
    this.returnLogin();
    this.opacity=1;
    
  },

  profilUpdate(){
    this.pageUpdate();
    
  },

  GetroleId(){
    var roleId=localStorage.getItem('roleId');
    this.roleId=roleId;
    console.log(this.roleId);
  },

  ProfilCreate(){
    var url='http://api.smartsense.com.tr/api/patient/addinfo'
    var token=localStorage.getItem('token');
    var selectkutu = document.getElementById('exampleFormControlSelect1');
    var selectkutu1 = document.getElementById('exampleFormControlSelect2');
		var selectkutu_value = selectkutu.options[selectkutu.selectedIndex].value;
    var selectkutu_value1 = selectkutu1.options[selectkutu1.selectedIndex].value;
    var JsonObjects={
      "identityNumber": this.Tckn,
      "gender": Number(selectkutu_value),
      "dateOfBirth": this.bdate,
      "userStatus": Number(selectkutu_value1),
      "diagnosis": this.teshis
    }
    console.log(JsonObjects);
    axios.post(url,JsonObjects,{
        headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
        console.log(res);
        this.returnLogin()
        this.geri();
        this.getProfiles();
    })
  },

  getProfiles(){
    var url='http://api.smartsense.com.tr/api/patient/getinfo'
    var token=localStorage.getItem('token');
    axios.get(url,{
        headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
        console.log(res);
        var profileCart=document.querySelector('.profile-cart');
        profileCart.style.opacity=1;
        if(res.data.code=="200"){
          this.pageMode=0;
          this.Tckn=res.data.identityNumber;
          this.bdate=res.data.dateOfBirth.slice(0,10);
          this.userStatus=res.data.userStatus;
          this.teshis=res.data.diagnosis;
          this.gender=res.data.gender;
          this.mail=res.data.email
        }
        else{
          this.pageMode=1;
        }
    })
  },

  signInButtonPressed(e) {
    console.log("Sign In Button Pressed");
    e.preventDefault();
  },

  returnLogin() {
    this.pageMode=0;
  },

  pageModes() {
    this.pageMode=1;
    },

  pageUpdate() {
    this.pageMode=3;
  },

  pageClear() {
    this.pageMode=4;
  }

  },  
}
</script>

<style  scoped>

@import '../assets/css/toast.css';

img {
  border-radius: 15px;
}
.profile-card {
  position: absolute;
  right: 0;
  bottom: 0;
}
@media screen and (min-width: 768px) {
  .imagex{
    width: 100%;
  }
  /* .dikkat{
    margin-left: 20%;
    margin-right: 25%;
  } */
}
@media screen and (max-width: 768px) {
  .imagex{
    width: 100%;
  }
}

</style>


