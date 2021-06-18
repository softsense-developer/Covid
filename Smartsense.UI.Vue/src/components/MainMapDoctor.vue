<template>
  <div>
    <main class="page-content">
      <div class="container">
        <div class="row mt-3" style="border-bottom:3px solid rgb(17,123,110);">
          <div class=" col-12 d-flex justify-content-center" style="pointer-events:none">
            <h5 style="color:rgb(17,123,110);">Konum</h5>
          </div>
        </div>
        <div class="row mt-5">
          <div class="col-sm-12 col-md-12 col-lg-12">
            <div class="account-update">
              <google-map></google-map>
            </div>
          </div>
        </div>
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