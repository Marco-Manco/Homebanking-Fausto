const urlApi = 'http://localhost:8080/rest/clients'

Vue.createApp({

  data() {
    return {
      clientes: [],
      firstName: "",
      lastName: "",
      email: "",
      data:"",

      loanName:"",
      maxAmount:"",
      payments: [],
      interestPercentage: ""
    }
  },

  created() {
    this.getClientsFromApi(urlApi)
    
  },

  methods: {
    async loadData(){
      try {
        const {data} = await axios.post(urlApi,{
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email
        })
      } catch (error) {
        console.error(error)
      }
    },
    cleanForm(){
      this.firstName = ""
      this.lastName = ""
      this.email = ""
    },
    
    
    async getClientsFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.clientes = data._embedded.clients
        this.data = data
      } catch (error) {
        console.error(error)
      } 
    },

    async aniadirCliente(){
      await this.loadData()
      this.cleanForm()
      await this.getClientsFromApi(urlApi)
    },
    async deleteClient(url){
      try {
        await axios.delete(url)
      } catch (error) {
        console.error(error)
      } 
      this.getClientsFromApi(urlApi)
    },
    logOut(){
      axios.post('/api/logout')
        .then(()=>{
          let [url] = location.href.split("admin")
          location.href = `${url}web/index.html`
        })
    },
    async createLoan(){
      try {
        await axios.post('/api/loans/new',
          {
            name: this.loanName,
            maxAmount: this.maxAmount,
            payments: this.payments,
            interestPercentage: this.interestPercentage
          },
          {headers:{'content-type':'application/json'}})
          location.reload()

      } catch (error) {
        this.errorMessage = error.response.data
        console.log(error.response.data)
      } 
    }
  },
  computed:{
    
  }

}).mount('#app')