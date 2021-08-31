<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">Doktorlar</h1>
    <div class="container">
        <div class="row">
      <div class="col-sm-12 col-lg-12 col-md-12 d-flex justify-content-end">
        <i class="fas fa-plus" style="font-size:15px;cursor:pointer;color:green;" data-toggle="modal" data-target="#exampleModalDoctorAdd"> Hasta rolünü değiştirmek için tıklayın</i>
        <div class="modal fade" id="exampleModalDoctorAdd" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <span class="headline">Doktor ekle</span>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                  <form  @submit.prevent="PromotionDoctor(userID)">
            <div class="card-text mt-2" style="margin-top:3%">
              <span class="spanım">Kullanıcı ID Numarası</span>
              <input type="text" class="form-control" v-model="userID" id="exampleInputPassword1"  :class="{ 'is-invalid': submitted && $v.userID.$error }"   >
              <div v-if="submitted && $v.userID.$error" class="invalid-feedback">
                <span v-if="!$v.userID.required">ID Numarası Boş Olamaz</span>
                <span v-if="!$v.userID.numeric"><br>Lütfen Sayısal Karakter giriniz</span>
              </div>
            </div>
             <div class="form-group mt-5 d-flex justify-content-center">
                <button class="btn btn-success w-100">Ekle</button>
             </div>
            </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-outline-danger" data-dismiss="modal">Close</button>
                <!-- <button type="button" class="btn btn-success" @click="gönder()">İlaç Ekle</button> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
      <div class="row ">
        <div class="col-sm-6 col-md-6 col-lg-6" v-for="(doc,index) in doctors" :key="index">
          <div class="card mt-3" style="width: 100%;" >
            <div class="card-body d-flex justify-content-between">
              <strong><p>{{doc.doctorName}} {{doc.doctorSurname}}</p></strong>
              <i class="fas fa-user-times" style="cursor:pointer;font-size:30px;color:#E57373" @click="addlocal(index,doc.doctorId)" data-toggle="modal" data-target="#exampleModal" ></i>
              <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <span class="headline">Rol düşür</span>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      Bu doktorun rolünü düşürmek istediğinize emin misiniz?
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-success" data-dismiss="modal">Hayır</button>
                      <button type="button" class="btn btn-outline-danger" @click="deletedoctor()">Evet</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { required,numeric } from "vuelidate/lib/validators";
import Vue from 'vue'
import Navbar from '@/components/Navbarsupervisor'
import axios from 'axios'
export default {
    name:'MainSupDoctors',
    data() {
        return {
            doctors:[],
            userID:'',
            submitted:false
        }
    },
    components:{
        Navbar
    },
    validations:{
        userID:{required,numeric}
        },
    methods: {
        addlocal(index,id) {
          localStorage.setItem('index',index);
          localStorage.setItem('doctorid',id);
        },
        PromotionDoctor(id) {
            this.submitted = true;
            this.$v.$touch();
            if (this.$v.$invalid) {
              return;
            }
          console.log(id);
           var url='http://api.smartsense.com.tr/api/supervisor/doctorpromotion'+`/${id}`
           var token=localStorage.getItem('token');
          axios.get(url, {
           headers: {'Authorization': `Bearer ${token}`}
           }).then(res=> {
               console.log(res);
              if(res.data.code == "200"){
                Vue.$toast.success("Doktora istek başarıyla gönderildi");
              }else if(res.data.code == "400") {
                Vue.$toast.warning(res.data.errors);
              }
            }).catch(err=> {
              Vue.$toast.error(err);
              })
        },
        deletedoctor() {
            var id=localStorage.getItem('doctorid')
            var index=localStorage.getItem('index')
            var url='http://api.smartsense.com.tr/api/supervisor/doctordemotion'+`/${id}`
            var token=localStorage.getItem('token');
            axios.get(url, {
            headers: {'Authorization': `Bearer ${token}`}
            }).then(res=> {
                if(res.data.code=="200") {
                  this.doctors.splice(index,1);
                  Vue.$toast.success('silme işlmei başarılı')
                }
          });
        },
        getAllDoctors(){
        var url='http://api.smartsense.com.tr/api/supervisor/gethospitaldoctors'
        var token=localStorage.getItem('token');
        axios.get(url, {
          headers: {'Authorization': `Bearer ${token}`}
        }).then(res=> {
          console.log(res);
          res.data.doctors.forEach(element => {
              console.log(element);
              this.doctors.push(element);
          });
        });
      },  

      
    },
    
    mounted() {
        this.getAllDoctors();
    }

}
</script>

<style scoped>
  .card {
    overflow: hidden;
    width: 100%;
    background: #fff;
    /* padding: 30px; */
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.2);
  }
</style>

