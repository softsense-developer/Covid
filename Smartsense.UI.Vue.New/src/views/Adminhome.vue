<template>
  <div>
      <Navbar/>
      <h1 class="subheading grey--text">Hastaneler</h1>
      <div class="container">
          <div class="row">
              <div class="col-sm-12 col-md-12 col-lg-12">
                  <div class="card">
                      <v-data-table
                   :headers="headers"
                   :items="desserts"
                   :search="search"
                  ></v-data-table>
                  </div>
                  
              </div>
          </div>
      </div>
  </div>
</template>

<script>
import axios from 'axios'
import Navbar from '@/components/Navbaradmin'
export default {
  name:'admin anasayfa',
  data () {
      return {
        search: '',
        warnings:[],
        headers: [
          {
            text: 'Hastane adı',
            align: 'start',
            filterable: false,
            value: 'name',
          },
          { text: 'Hastane adresi', value: 'valueType' },
          { text: 'Hastane kapasitesi', value: 'valueData' },
        ],
        desserts: [
          
        ],
      }
  },
  components :{
      Navbar
  },
  mounted() {
    this.getWarnings();
  },
  methods :{
      getWarnings () {
        var token=localStorage.getItem('token');
      var url='http://api.smartsense.com.tr/api/admin/GetAllHospital'
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        res.data.hospitals.forEach(element => {
            var jsonobject={}
            jsonobject.name=element.hospitalName;
            jsonobject.valueType=element.hospitalAddress;
            jsonobject.valueData=element.hospitalCapacity;

            this.desserts.push(jsonobject);
        });
      })
      }
  }
}
</script>

<style scoped>
  .card{
    overflow: hidden;
    width: 100%;
    background: #fff;
    padding: 30px;
    border-radius: 5px;
    box-shadow: 0px 15px 20px rgba(0,0,0,0.1);
  }
</style>

