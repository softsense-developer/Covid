<template>
  <div>
      <Navbar/>
      <h1 class="subheading grey--text">Hasta konumları</h1>
      <div class="container">
        <div class="row mt-5">
          <div class="col-sm-12 col-md-12 col-lg-12">
            
              <GoogleMap/>
            
          </div>
        </div>
      </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbarcompanian'
import GoogleMap from '@/components/GoogleMapCompanian'
export default {
    name:"MainMapDoctor",
    components:{
        GoogleMap,
        Navbar
    }

}
</script>

<style>

</style>