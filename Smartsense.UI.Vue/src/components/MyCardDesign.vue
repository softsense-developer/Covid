<template>
  <div class="card">
          <div class="container">
            <div class="row" >
              <div class="col-sm-6 col-md-6 col-lg-6 d-flex justify-content-start" >
                <img :src="'../assets/'+ mysrc.joke" >
              </div>
              <div class="col-sm-6 col-md-6 col-lg-6">
                <h6 class="mt-3"><span style="color:rgb(28,134,122)">hastane:</span> asgasg</h6>
                <h6><span  style="color:rgb(28,134,122)">Adres:</span> asgasg</h6>
                <h6><span  style="color:rgb(28,134,122)">hastane kapasite:</span> asgasgasg</h6>
                <div class="text-right" style="margin-top:20%">
                  <button class="btn text-center w-100 text-white shadow">Güncelle</button>
                </div>
              </div>
            </div>
          </div>
        </div>
</template>

<script>
export default {
    name:'mainsuphospital',

    data() {
        return {
            mysrc:{
                joke:"hospital.jpg"
            }
          } 
        },

    methods: {

    },

    mounted() {
        console.log(this.mysrc.joke);
    }

}
</script>

<style>
 /*
=============== 
Fonts
===============
*/
@import url("https://fonts.googleapis.com/css?family=Open+Sans|Roboto:400,700&display=swap");

/*
=============== 
Variables
===============
*/

:root {
  /* dark shades of primary color*/
  --clr-primary-1: hsl(205, 86%, 17%);
  --clr-primary-2: hsl(205, 77%, 27%);
  --clr-primary-3: hsl(205, 72%, 37%);
  --clr-primary-4: hsl(205, 63%, 48%);
  /* primary/main color */
  --clr-primary-5: hsl(205, 78%, 60%);
  /* lighter shades of primary color */
  --clr-primary-6: hsl(205, 89%, 70%);
  --clr-primary-7: hsl(205, 90%, 76%);
  --clr-primary-8: hsl(205, 86%, 81%);
  --clr-primary-9: hsl(205, 90%, 88%);
  --clr-primary-10: hsl(205, 100%, 96%);
  /* darkest grey - used for headings */
  --clr-grey-1: hsl(209, 61%, 16%);
  --clr-grey-2: hsl(211, 39%, 23%);
  --clr-grey-3: hsl(209, 34%, 30%);
  --clr-grey-4: hsl(209, 28%, 39%);
  /* grey used for paragraphs */
  --clr-grey-5: hsl(210, 22%, 49%);
  --clr-grey-6: hsl(209, 23%, 60%);
  --clr-grey-7: hsl(211, 27%, 70%);
  --clr-grey-8: hsl(210, 31%, 80%);
  --clr-grey-9: hsl(212, 33%, 89%);
  --clr-grey-10: hsl(210, 36%, 96%);
  --clr-white: #fff;
  --clr-red-dark: hsl(360, 67%, 44%);
  --clr-red-light: hsl(360, 71%, 66%);
  --clr-green-dark: hsl(125, 67%, 44%);
  --clr-green-light: hsl(125, 71%, 66%);
  --clr-black: #222;
  --ff-primary: "Roboto", sans-serif;
  --ff-secondary: "Open Sans", sans-serif;
  --transition: all 0.3s linear;
  --spacing: 0.1rem;
  --radius: 0.25rem;
  --light-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  --dark-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  --max-width: 1170px;
  --fixed-width: 620px;
}
ul {
  list-style-type: none;
}
a {
  text-decoration: none;
}
h1,
h2,
h3,
h4 {
  letter-spacing: var(--spacing);
  text-transform: capitalize;
  line-height: 1.25;
  margin-bottom: 0.75rem;
  font-family: var(--ff-primary);
}
h1 {
  font-size: 3rem;
}
h2 {
  font-size: 2rem;
}
h3 {
  font-size: 1.25rem;
}
h4 {
  font-size: 0.875rem;
}
h6{
  border-bottom: 1px solid black;
  text-transform: capitalize;
  padding-top: 3%;
}
.spanım{
  /* text-transform: uppercase; */
  font-size: 12px;
  text-transform: capitalize;
  color:rgb(129, 120, 120)
}
p {
  margin-bottom: 1.25rem;
  color: var(--clr-grey-5);
}
@media screen and (min-width: 800px) {
  h1 {
    font-size: 4rem;
  }
  h2 {
    font-size: 2.5rem;
  }
  h3 {
    font-size: 1.75rem;
  }
  h4 {
    font-size: 1rem;
  }
  body {
    font-size: 1rem;
  }
  h1,
  h2,
  h3,
  h4 {
    line-height: 1;
  }
}
/*  global classes */

/* section */
@media screen and (min-width: 992px) {
  
}

#person-img {
  width: 100%;
  display: block;
  height: 85%;
  object-fit: cover;
  border-radius: 10%;
  position: relative;
  padding-left: 0  !important;
  margin-top: 4%;
}
.shadow{
  background:rgb(28,134,122)
}
.shadow:hover{
  background:rgb(24, 114, 106)
}





</style>