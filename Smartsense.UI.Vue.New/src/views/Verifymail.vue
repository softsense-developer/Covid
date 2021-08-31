<template>
  <div>
    <div class="container">
        <div class="row">
            <div class="col-sm-12 col-md-12 col-lg-12 d-flex justify-content-center mt-5 ">
                <div class="card w-75">
                    <div class="card-header d-flex justify-content-center  "><img src="https://smartsense.com.tr/img/ss-logo.PNG" alt="" width="75px" height="75px"></div>
                    <div class="card-body ">
                    <p class="card-text d-flex justify-content-center  text-success" style="font-size:21px">Hesabınız Başarıyla Aktifleştirildi.<i class="fas fa-check text-success "  style="font-size:35px"></i></p>
                    <div class="d-flex justify-content-center">
                        <router-link to="/home" tag="a" class="btn btn-outline-success" style="margin:auto">Siteye Yönlendir</router-link>
                    </div>
                    </div>
                </div>
             </div>
         </div>
    </div>
</div>
</template>

<script>
import axios from 'axios'
export default {
    name:'VerifyEmail',
    data(){
        return{

        }
    },
    methods:{

    },
    created(){
        console.log(this.$route.params.guid);
        var guid=this.$route.params.guid;
        var url='http://api.smartsense.com.tr/api/auth/verify'+`/${guid}`
        axios.get(url,{
        }).then(res=>{
            console.log(res);
        })


    }
}
</script>

<style>

</style>