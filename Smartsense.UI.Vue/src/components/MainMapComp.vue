<template>
  <div>
    <main class="page-content">
      <div class="account-update">
        <google-map-comp></google-map-comp>
      </div>
    </main>
  </div>
</template>

<script>
import GoogleMapComp from './GoogleMapComp.vue'
export default {
    name:"MainMapDoctor",
    components:{
        GoogleMapComp
    }

}
</script>

<style>

</style>