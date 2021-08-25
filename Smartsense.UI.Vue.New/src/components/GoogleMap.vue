<template>
  <div>

    <gmap-map
      :center="center"
      :zoom="6"
      style="width:100%;  height: 500px;"
    >
      <gmap-marker
        :key="index"
        v-for="(m, index) in markers"
        :position="m"
        @click="center=m"
        :title="m.name+': '+m.time.split('T')"
      ></gmap-marker>
    </gmap-map>

  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "GoogleMap",
  data() {
    return {
      center: { lat: 38.6686133, lng: 39.1694533 },
      markers: [],
      currentPlace: null,
    };
  },

  mounted() {
    this.geolocate();
  },

  methods: {
    setPlace(place) {
      this.currentPlace = place;
    },
    geolocate: function() {

        navigator.geolocation.getCurrentPosition(position => {
          this.center = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };
        });
      var token=localStorage.getItem('token');
      var url=`http://api.smartsense.com.tr/api/doctor/getdoctorlocations`;
      axios.get(url,{
          headers:{'Authorization': `Bearer ${token}`},
      }).then(res=>{
          console.log(res);
          res.data.patients.forEach(element => {
              console.log(element);
              this.markers.push(element);
          });
      })
    }
  }
};
</script>