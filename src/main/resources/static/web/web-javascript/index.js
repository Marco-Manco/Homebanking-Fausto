const urlApi = `/api/clients/current`
Vue.createApp({

  data() {
    return {
      showErrorLoginMessage: false,
      email: "",
      password: "",
      client: {},
      registrationFirstName: "",
      registrationLasttName: "",
      registrationEmail: "",
      registrationPassword: ""
    }
  },

  created() {
    
  },

  methods: {
    async loginClient(){
      try {
        await axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        this.redirect()
      } catch (error) {
        console.log("no se pudo loguear")
        this.showErrorLoginMessage  = true
        console.log(error.response.data)
      } 
    },
    async registerClient(){
      try {
        await axios.post('/api/clients',`firstName=${this.registrationFirstName}&lastName=${this.registrationLasttName}&email=${this.registrationEmail}&password=${this.registrationPassword}` ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        await axios.post('/api/login',`email=${this.registrationEmail}&password=${this.registrationPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        this.redirect()
        
      } catch (error) {
        console.log("no se pudo registrar")
        console.log(error.response.data)
      }
    },
    // goToAccounts(){
    //   let [url] = location.href.split("web")
    //   location.href = `${url}web/accounts.html`
    //   console.log(location.href)
    // },
    redirect(){
      let [url] = location.href.split("web")
      let [,adminEmail] = this.email.split("@")
      if(adminEmail == "admin"){
        location.href = `${url}admin/manager.html`
      }
      else{
        location.href = `${url}web/accounts.html`
      }
    }
  },
  computed:{
  }

}).mount('#app')