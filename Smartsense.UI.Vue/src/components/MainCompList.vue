<template>
  <div>
    <div class="myDiv" style="width:1px ;height:1px;"></div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
          <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Refakatçılarım</h5>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-sm-6 col-md-6 col-lg-6" v-for="companian of companians" :key="companian.id" >
            <div class="card mt-3" style="width: 100%;" >
              <div class="card-body d-flex justify-content-between">
              <strong><p>{{companian.name}} {{companian.surname}}</p></strong>
              <i class="fas fa-user-times" style="cursor:pointer;font-size:30px;color:#E57373" @click="deleteCompanian(companian.userid)" ></i>
              <!-- <i class="fas fa-angle-double-down card-link" style="cursor:pointer"></i> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name:'MainAdminHome',
  data() {
    return { 
      companians:[]
    }
  },
  methods: {

    getProfiles() {
      var url='http://api.smartsense.com.tr/api/patient/getcompanions'
      var token=localStorage.getItem('token');
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
        console.log(res);
        if(res.data.code=="200") {
          console.log(res);
          res.data.companionModels.forEach(element => {
            console.log(element);
            this.companians.push(element);
          });
        }
        })
    },
    deleteCompanian(id) {
      var html=document.querySelector('.myDiv');
      html.innerHTML=`
        <button type="button" class="btn btn-primary modal-div mt-3" data-toggle="modal" data-target="#exampleModal" style="width:1px;height:1px;">
        </button>
        <!-- Modal -->
        <div class="modal fade mt-5" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            <div class="modal-header" style="border-bottom: none;">
                <h5 class="modal-title" id="exampleModalLabel"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <span style="text-transform:capitalize;">refakatçıyı silmek istediğinize emin misiniz?</span>
            </div>
            <div class="modal-footer" style="border-top:none;">
                <button type="button-outline-danger" class="btn btn-danger" data-dismiss="modal">Hayır</button>
                <button type="button" class="btn  success-modal" style="background:-webkit-linear-gradient(left, #3ca89c, #00695C);color:white;">Evet</button>
            </div>
            </div>
        </div>
        </div>`
        var modal_div=document.querySelector('.modal-div');
        modal_div.click();
        var success_modal=document.querySelector('.success-modal');
        success_modal.addEventListener('click',function(){
          var url='http://api.smartsense.com.tr/api/patient/deletecompanion'+`/${id}`
            var token=localStorage.getItem('token');
            axios.get(url, {
            headers: {'Authorization': `Bearer ${token}`}
            }).then(res=> {
            console.log(res);
          });
        });
      },

  },
  mounted() {
    this.getProfiles()
  }

}
</script>

<style>

.card {
  border-radius: 10px;
  border: none;
  box-shadow: 0 15px 15px rgba(0, 0, 0, 0.1);
}

</style>