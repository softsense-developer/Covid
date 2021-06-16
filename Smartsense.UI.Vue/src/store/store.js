import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex);

export const store=new Vuex.Store({

    state:{
      user:""
    },

    mutations:{
      user(state,user){
          state.user=user
      }

    },
    actions:{
      user(context,user){
       context.commit('user',user);
      }
    },
    getters:{
      isAuthenticated(state){
          return state.token!=="";
      }
    }

})
export default store