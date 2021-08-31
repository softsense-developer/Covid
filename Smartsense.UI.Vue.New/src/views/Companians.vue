<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">Refakatçılar</h1>
    <div class="container">
      <div class="row ">
        <div class="col-sm-6 col-md-6 col-lg-6" v-for="(doc,index) in doctors" :key="index">
          <div class="card mt-3" style="width: 100%;" >
            <div class="card-body d-flex justify-content-between">
              <strong><p>{{doc.name}} {{doc.surname}}</p></strong>
              <i class="fas fa-user-times" style="cursor:pointer;font-size:30px;color:#E57373" @click="addlocal(index,doc.userid)" data-toggle="modal" data-target="#exampleModal" ></i>
              <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <span class="headline">Refakatçı sil</span>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                      Hasta yakınını silmek istediğinizden emin misiniz?
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
import Navbar from '@/components/Navbar'
import axios from 'axios'
export default {
    name:'MainSupDoctors',
    data() {
        return {
            doctors:[],
            userID:'',
            submitted:false,
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
          localStorage.setItem('companianid',id);
        },
        
        deletedoctor() {
            var id=localStorage.getItem('companianid')
            var index=localStorage.getItem('index')
            var url='http://api.smartsense.com.tr/api/patient/deletecompanion'+`/${id}`
            var token=localStorage.getItem('token');
            axios.get(url, {
            headers: {'Authorization': `Bearer ${token}`}
            }).then(res=> {
                if(res.data.code=="200") {
                  this.doctors.splice(index,1);
                  Vue.$toast.success('silme işlemi başarılı')
                }
          });
        },
        getAllDoctors(){
        var url='http://api.smartsense.com.tr/api/patient/getcompanions'
        var token=localStorage.getItem('token');
        axios.get(url, {
          headers: {'Authorization': `Bearer ${token}`}
        }).then(res=> {
          console.log(res);
          res.data.companionModels.forEach(element => {
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

