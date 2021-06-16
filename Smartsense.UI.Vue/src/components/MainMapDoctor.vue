<template>
  <div>
    <main class="page-content">
      <div class="account-update">
        <google-map></google-map>
      </div>
    </main>
  </div>
</template>

<script>
import GoogleMap from './GoogleMap.vue'
export default {
    name:"MainMapDoctor",
    components:{
        GoogleMap
    }

}
</script>

<style>

</style>