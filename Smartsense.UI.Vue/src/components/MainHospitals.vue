<template>
  <div>
    <main class="page-content" style="background: -webkit-linear-gradient(left, rgb(241,245,248), rgb(241,245,248));">
      <div class="container">
        <div class="row d-flex justify-content-center">
          <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center" style="border-bottom:1px solid black;">
             <h5 style="color:#607D8B;text-transform: capitalize;">Hastaneler</h5>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-sm-6 col-md-6 col-lg-6" v-for="hospitals of hospitals" :key="hospitals.id">
            <div class="card mt-2" style="width: 100%;">
               <!-- <img class="card-img-top" src="..." alt="Card image cap"> -->
              <div class="card-body">
                <h5 class="card-title" style="text-transform:capitalize">{{hospitals.hospitalName}}</h5>
                <p class="card-text">Adresi: {{hospitals.hospitalAddress}}</p>
                <p class="card-text">Kapasitesi: {{hospitals.hospitalCapacity}}</p>
                <!-- <a href="#" class="btn btn-primary">Go somewhere</a> belki delete işlemi gelir-->
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
      hospitals:[]
    }
  },
  methods: {
    getallHospital() {
      var token=localStorage.getItem('token');
      var url=`http://api.smartsense.com.tr/api/admin/GetAllHospital`
      axios.get(url,{
        headers:{'Authorization': `Bearer ${token}`}
       }).then(res=>{
        console.log(res);
        res.data.hospitals.forEach(element => {
        console.log(element);
        this.hospitals.push(element);
        });
      })
    }

  },
  mounted() {
    this.getallHospital();
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