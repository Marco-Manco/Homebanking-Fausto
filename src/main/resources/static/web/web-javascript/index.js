const urlApi = `http://localhost:8080/api/clients/current`
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
    // axios.post('/api/clients',"firstName=pedro2&lastName=rodriguez&email=pedro@mindhub.com&password=pedro",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('registered'))
    
  },

  methods: {
    async loginClient(){
      try {
        await axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        this.goToAccounts()
      } catch (error) {
        console.log("no se pudo loguear")
        this.showErrorLoginMessage  = true
        console.error(error)
      } 
    },
    async registerClient(){
      try {
        await axios.post('/api/clients',`firstName=${this.registrationFirstName}&lastName=${this.registrationLasttName}&email=${this.registrationEmail}&password=${this.registrationPassword}` ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        await axios.post('/api/login',`email=${this.registrationEmail}&password=${this.registrationPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        this.goToAccounts()
        
      } catch (error) {
        console.log("no se pudo registrar")
        console.log(error.response.data)
      }
    },
    goToAccounts(){
      let [url] = location.href.split("web")
      location.href = `${url}web/accounts.html`
      console.log(location.href)
    },
  },
  computed:{
  }

}).mount('#app')