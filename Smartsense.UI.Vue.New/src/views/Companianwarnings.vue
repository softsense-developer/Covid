<template>
  <div>
    <Navbar/>
    <h1 class="subheading grey--text">Uyarılar</h1>
    <div class="container">
        <div class="row">
            <div class="col-sm-12 col-md-12 col-lg-12">
                <v-card>
    <v-card-title>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="desserts"
      :search="search"
    ></v-data-table>
  </v-card>
            </div>
        </div>
    </div>
    
  </div>
</template>

<script>
import Navbar from '@/components/Navbarcompanian'
import axios from 'axios'
export default {
  name:'doctorwarnings',
  data () {
      return {
        search: '',
        warnings:[],
        headers: [
          {
            text: 'İsim Soyisim',
            align: 'start',
            filterable: false,
            value: 'name',
          },
          { text: 'Uyarı tipi', value: 'valueType' },
          { text: 'Uyarı değeri', value: 'valueData' },
          { text: 'Uyarı tarihi', value: 'date' },
          { text: 'Uyarı saati', value: 'hours' },
        ],
        desserts: [
          
        ],
      }
  },
  components : {
      Navbar
  },
  mounted () {
      this.getWarnings();
  },
  methods : {
      getWarnings () {
        var token=localStorage.getItem('token');
      var url='http://api.smartsense.com.tr/api/companion/warninglist'
      axios.get(url, {
        headers:{'Authorization': `Bearer ${token}`}
      }).then(res=> {
        console.log(res);
        res.data.warnings.forEach(element => {
            var jsonobject={}
            jsonobject.name=element.name;
            if(element.valueType == 0) {
                jsonobject.valueType="oksijen"
            }else if (element.valueType == 1) {
                jsonobject.valueType="nabız"
            }else if (element.valueType == 2) {
                jsonobject.valueType="sıcaklık"
            }else if(element.valueType == 3) {
                jsonobject.valueType="bağlantı durumu"
            }else if(element.valueType == 4) {
                jsonobject.valueType="nabız"
            }
            if(element.valueData == 0) {
              jsonobject.valueData="bağlantı koptu";
            } else if(element.valueData == 1) {
              jsonobject.valueData="bağlantı koptu";
            } else {
              jsonobject.valueData=element.valueData;
            }
            
            jsonobject.date=element.time.slice(0,10)
            jsonobject.hours=element.time.slice(11,19)

            this.desserts.push(jsonobject);
        });
      })
      }
    
  }
}
</script>

<style>

</style>