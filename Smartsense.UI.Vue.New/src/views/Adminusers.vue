<template>
  <div>
      <Navbar/>
      <h1 class="subheading grey--text">Tüm kullanıcılar</h1>
      <div class="container">
        <div class="row mt-3">
          <div class="col-sm-6 col-md-6 col-lg-6" v-for="users of users" :key="users.id">
            <div class="card mt-2" style="width: 100%;">
               <!-- <img class="card-img-top" src="..." alt="Card image cap"> -->
              <div class="card-body ">
                <h5 class="card-title" style="text-transform:capitalize">{{users.id}}-) {{users.name}} {{users.surName}}</h5>
                <p class="card-text">{{users.email}}<i class="fas fa-check ml-2 text-success"></i></p>
                <!-- <a href="#" class="btn btn-primary">Go somewhere</a> belki delete işlemi gelir-->
                <div class="card-text text-right">
                  <button :key="users.id" class="badge badge-success" @click="goToHospitalSelect(users.id,users.name+' '+users.surName)" > Başhekim Seçim</button> 
                </div>
                
              </div>
            </div>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbaradmin'
import axios from 'axios'
export default {
  name:'MainAdminHome',
  data() {
    return { 
      users:[]
    }
  },
  components:{
    Navbar
  },
  methods: {
    goToHospitalSelect(prodId,name) {
    this.$router.push({name:'hospitalselect',params:{Pid:prodId,name:name}})
    },
    page(id) {
      console.log(id);
    },

    getallUser() {
      var token=localStorage.getItem('token');
      var url=`http://api.smartsense.com.tr/api/admin/GetAllUsers`
      axios.get(url,{
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=>{
        console.log(res);
        res.data.usersList.forEach(element => {
          console.log(element);
          this.users.push(element);
        });
      })
    }

  },
  mounted() {
    this.getallUser();
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