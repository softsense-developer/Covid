<template>
  <div>
    <main class="page-content">
      <!-- <div class="mt-5 text-center d-flex justify-content-center" v-if="patients.length==0">
          <scaling-squares-spinner class="text-info"
            :animation-duration="1000"
            :size="75"
            :color="'#0f796c'"
          />
        </div> -->
      <div class="container">
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Uyarılar</h5>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col-12">
            <input type="text" id="myInput" @keyup="myFunction()" placeholder="Hasta adını yazınız..." title="Type in a name">
          </div>
        </div>
      <div class="row d-flex justify-content-center">
        <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center">
          <div class="card w-100">
            <div class="container">
              <div class="row">
                <div class="col-12"></div>
              </div>
            </div>
            <table id="myTable" class="table table-borderless card-body">
            <tr class="header">
                <th >Hasta</th>
                <th >Uyarı Tipi</th>
                <th >Uyarı Değeri</th>
                <th >Saat</th>
                <th >Tarih</th>
              </tr>
              <tr v-for="item of patients" :key="item.id" >
                <td>{{item.name}}</td>
                <td v-if="item.valueType==4">Konum</td>
                <td v-if="item.valueType==0">Oksijen</td>
                <td v-if="item.valueType==1">Nabız</td>
                <td v-if="item.valueType==2">Sıcaklık</td>
                <td v-if="item.valueType==3">Bağlantı</td>
                <td v-if="item.value>1 ">{{item.value}}</td>
                <td v-if="item.value==0">Bağlantı Koptu</td>
                <td v-if="item.value==1">Bağlanıldı</td>
                <td>{{item.date}}</td>
                <td>{{item.time}}</td>
              </tr>
            </table>
          </div>
   
          <!-- <ul id="myUL" >
            <li v-for="item of patients" :key="item.id" class="mt-2"><div>
              <table class="table table-hover">
            <thead>
              <tr style="text-transform: capitalize;">
                <th scope="col">hasta</th>
                <th scope="col">uyarı tipi</th>
                <th scope="col">uyarı değerı</th>
                <th scope="col">zaman</th>
              </tr>
            </thead>
            <tbody class="body">
              <tr>
                <td>{{item.name}}</td>
                <td>{{item.type}}</td>
                <td v-if="item.value!=0 && item.value!=1">{{item.value}}</td>
                <td v-if="item.value==0">bağlantı koptu</td>
                <td v-if="item.value==1">bağlantı başarılı</td>
                <td>{{item.time}}</td>
              </tr>
            </tbody>
          </table> 
              </div></li>
          </ul> -->
        </div>
      </div>
       </div>
    </main>
    </div>

</template>

<script>
import axios from 'axios'
// import {ScalingSquaresSpinner} from 'epic-spinners'

// hasta ıd, hasta adı ve soyadı, uyarı tıpı, uyarı değerı, zaman
// bu data zaman sıralı ve en güncelı en üstte olacak şekılde geıtırılmesı lazım

// patıens ıçerısınde warnınglerın ıçerısınde 

export default {

  data(){
    return{
      x:'',
      patients:[],
      time:[],
      intervals:''
    }
  },
  components:{
    // ScalingSquaresSpinner
  },
  mounted(){
    this.intervals=setInterval(this.getpatientRequiest, 2000);
    
  },
  destroyed(){
      clearInterval(this.intervals);
    },
  methods:{
    getpatientRequiest(){
      var token=localStorage.getItem('token');
    var url='http://api.smartsense.com.tr/api/companion/warninglist'
    axios.get(url,{
      headers:{'Authorization': `Bearer ${token}`}
    }).then(res=>{
      console.log(res);
      var counter = 1;
      var patData = [];
      res.data.warnings.forEach(element => {
        // console.log(element);
        var patientData = {};
        patientData.id=counter;
        patientData.name=element.name;
        patientData.valueType=element.valueType;
        patientData.value=element.valueData;
        patientData.date=element.time.slice(0,10);
        patientData.time=element.time.slice(11,19);
        patData.push(patientData);
        counter++;
      });
      console.log(patData)
      // var counter = 1;
      // var patData = [];
      //  res.data.patients.forEach(patient => {
      //     patient.valueTypeListModels.forEach(el=>{
              // el.locations.forEach(location=>{
              //   var patientData = {};
              //   patientData.id=counter;
              //   patientData.name=patient.name + ' ' + patient.surname;
              //   patientData.type='location';
              //   patientData.value=location.valueData;
              //   patientData.time=location.time;
              //   patData.push(patientData);
              //   counter++;
              // })
              //  el.oxygen.forEach(ox=>{
              //   var patientData = {};
              //   patientData.id=counter;
              //   patientData.name=patient.name + ' ' + patient.surname;
              //   patientData.type='oxygen';
              //   patientData.value=ox.valueData;
              //   patientData.date=ox.time.slice(0,10);
              //   patientData.time=ox.time.slice(11,19);
              //   patData.push(patientData);
              //   counter++;
              // })
              // el.pulses.forEach(pulse=>{
              //   var patientData = {};
              //   patientData.id=counter;
              //   patientData.name=patient.name + ' ' + patient.surname;
              //   patientData.type='pulses';
              //   patientData.value=pulse.valueData;
              //   patientData.date=pulse.time.slice(0,10);
              //   patientData.time=pulse.time.slice(11,19);
              //   patData.push(patientData);
              //   counter++;
              // })
              // el.temperatures.forEach(temp=>{
              //   var patientData = {};
              //   patientData.id=counter;
              //   patientData.name=patient.name + ' ' + patient.surname;
              //   patientData.type='temperature';
              //   patientData.value=temp.valueData;
              //   patientData.date=temp.time.slice(0,10);
              //   patientData.time=temp.time.slice(11,19)
              //   patData.push(patientData);
              //   counter++;
              // })
              // el.connections.forEach(connection=>{
              //   var patientData={};
              //   patientData.id=counter;
              //   patientData.name=patient.name + ' ' + patient.surname;
              //   patientData.type='connection';
              //   patientData.value=connection.valueData;
              //   patientData.date=connection.time.slice(0,10);
              //   patientData.time=connection.time.slice(11,19)
              //   patData.push(patientData);
              //   counter++;
              // })
      //    })
      // });
 
       this.patients = patData.sort((a, b) => b.time - a.time);
      // console.log(this.patients);
     
       /*
      res.data.patients.forEach(element => {
        console.log(element);
        this.patients.push(element.valueTypeListModels)    
        }); 
        console.log(this.patients);
        this.patients.forEach(el=>{
          console.log(el);
          console.log(el[0].oxygen);
      })
      */
    })
    },
    myFunction() {
      var input, filter, table, tr, td, i, txtValue;
      input = document.getElementById("myInput");
      filter = input.value.toUpperCase();
      table = document.getElementById("myTable");
      tr = table.getElementsByTagName("tr");
      for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
          if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
              tr[i].style.display = "";
            } else {
              tr[i].style.display = "none";
              }
            }       
          }
        }    
  }
}
</script>

<style scoped>
@import '../assets/css/question.css'; 

@media screen and (min-width: 768px) {
  /* .question {
    padding-right: 0 !important;
  } */
}
.danger{
  position: absolute;
  right: 0;

}
#myInput {
  width: 100%; /* Full-width */
  font-size: 12px; /* Increase font-size */
  padding: 12px 20px 12px 40px; /* Add some padding */
  border: 1px solid #ddd; /* Add a grey border */
  margin-bottom: 12px; /* Add some space below the input */
  border-radius: 5px;
}



#myTable tr.header, #myTable tr:hover {
  /* Add a grey background color to the table header and on hover */
  background-color: #ffffff;
}




</style>


