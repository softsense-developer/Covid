<template>
  <div>
    <Navbar id="sidebar"/>
    <h1 class="subheading grey--text">İstekler</h1>
    <div class="container">
        <div class="row">
          <div class="col-sm-12 col-md-12 col-lg-12 text-center">
              <blockquote class="blockquote mb-0" v-if="pageMode==1">
                <footer class="blockquote-footer" style="font-size:25px;">Herhangi bir<cite title="Source Title"> istek bulunmamaktadır</cite></footer>
              </blockquote>   
          </div>
            <div class="col-sm-12 col-md-12 col-lg-12" v-for="(requiests,index) in requiests" :key="index">
              <div class="card-requiest mt-3" >
                  <div class="card-body d-flex justify-content-between" >
                    <p class="card-title ">{{requiests.name}} {{requiests.surname}}</p>
                    <div class="card-title ">
                      <button class="badge badge-success mr-1 p-2" @click="addstorageId(requiests.id,index)"  data-toggle="modal" data-target="#exampleModal" >Kabul</button>
                      <button class="badge badge-danger p-2" @click="addstorageId(requiests.id,index)" data-toggle="modal" data-target="#exampleModalreject" >Red Et</button>
                      <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title" id="exampleModalLabel">Insense</h5>
                              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                            <div class="modal-body">
                              Bu isteği kabul etmek istediğinize emin misiniz?
                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-success" data-dismiss="modal">Hayır</button>
                              <button type="button" class="btn btn-outline-danger" @click="postrequiestAccept()" data-dismiss="modal">Evet</button>
                            </div>
                          </div>
                        </div>
                      </div>          
                      <div class="modal fade" id="exampleModalreject" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title" id="exampleModalLabel">Insense</h5>
                              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                            <div class="modal-body">
                              Bu isteği red etmek istediğinize emin misiniz?
                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-success" data-dismiss="modal">Hayır</button>
                              <button type="button" class="btn btn-outline-danger" @click="postrequiestReject()" data-dismiss="modal">Evet</button>
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
  </div>
</template>

<script>
import Vue from 'vue'
import axios from 'axios'
import Navbar from '@/components/Navbar'
export default {
  name:'doctorrequiest',
  components :{
    Navbar
  },
  data () {
      return {
        requiests:[],
        pageMode:0
      }
  },
  mounted() {
    this.getRequiests();
  },
  methods : {
      getRequiests() {
      var url='http://api.smartsense.com.tr/api/patient/getpromotions';
      var token=localStorage.getItem('token');
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
        console.log(res);
        if(res.data.connectionRequests.length== 0) {
          this.pageMode=1;
        }
        res.data.connectionRequests.forEach(element => {
            this.requiests.push(element);
        });
      }).catch(err=> {
        Vue.$toast.error(err);
        this.$router.push('/error');//buranın error durumuna göre değişmesi gerekecek
      })
    },
    addstorageId(id,index) {
      localStorage.setItem("patientsId",id);
      localStorage.setItem("index",index)
    },

    postrequiestAccept() {
      var patientId=localStorage.getItem('patientsId')
      var index=localStorage.getItem("index");
      var url='http://api.smartsense.com.tr/api/patient/connectionacceptrefuse';
      var token=localStorage.getItem('token');
      var object={
      "id": patientId,
      "isAccept": true
      }
      axios.post(url, object, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        this.requiests.splice(index,1);
        Vue.$toast.success("istek kabul edildi");
        if(this.requiests.length == 0) {
          this.pageMode=1;
        }
      }).catch(err=> {
        Vue.$toast.error(err);
        this.$router.push('/error');//buranın error durumuna göre değişmesi gerekecek
      })
      

      
    },
    postrequiestReject() {
      var patientId=localStorage.getItem('patientsId')
      var index=localStorage.getItem("index");
      var url='http://api.smartsense.com.tr/api/patient/connectionacceptrefuse';
      var token=localStorage.getItem('token');
      var object={
      "id": patientId,
      "isAccept": false
      }
      axios.post(url, object, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        this.requiests.splice(index,1);
        Vue.$toast.success("istek kabul edildi");
        if(this.requiests.length == 0) {
          this.pageMode=1;
        }
      }).catch(err=> {
        Vue.$toast.error(err);
        this.$router.push('/error');//buranın error durumuna göre değişmesi gerekecek
      })
      

      
    }
  }
}
</script>

<style scoped>

  .card-requiest {
    border-top: 1px solid green;
    display: flex;
    width: 100%;
    height: 75%;
    background: rgba(255, 255, 255, 0.375);
    box-shadow: 0 0.75rem 2rem 0 rgb(0 0 0 / 10%);
    border-radius: 2rem;

  }
</style>