<template>
  <div>
    <main class="page-content">
      <div class="account-update">
        <google-map-sup></google-map-sup>
      </div>
    </main>
  </div>
</template>

<script>
import GoogleMapSup from './GoogleMapSup.vue'
export default {
    name:"MainMapDoctor",
    components:{
        GoogleMapSup
    }

}
</script>

<style>

</style>