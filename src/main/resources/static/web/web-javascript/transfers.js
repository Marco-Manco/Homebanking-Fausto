const urlApi = `http://localhost:8080/api/clients/current/accounts`
Vue.createApp({
  data() {
    return {
      accounts: [],
      destinationAccount:"",
      amount: 0,
      description: "",
      externalDestinationAccount: "",
      sourceAccount: "",
      arrivalAccount:""
    }
  },

  created() {
    this.getAccountsFromApi(urlApi)
  },

  methods: {
    async getAccountsFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.accounts = data
      } catch (error) {
        console.error(error)
      } 
    },
    logOutClient(){
      axios.post('/api/logout')
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },
    async transfer(){
      try {
        await axios.post('/api/transactions',`transactionAmount=${this.amount}&description=${this.description}&sourceAccountNumber=${this.sourceAccount}&destinationAccountNumber=${this.arrivalAccount}` ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        //mostrar algun mensaje o algo de q se creo la cuenta en el html
        console.log("succesful transaction")
      } catch (error) {
        console.log(error.response.data)
      }
    }
  },
  computed:{
    
  }

}).mount('#app')