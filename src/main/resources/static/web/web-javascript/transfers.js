
let form = document.querySelector("form")
const urlApi = `/api/clients/current/accounts`
Vue.createApp({
  data() {
    return {
      accounts: [],
      destinationAccount:"",
      amount: 1,
      description: "",
      externalDestinationAccount: "",
      sourceAccount: "",
      arrivalAccount:"",
      
      mostrarDatos: true,
      transferenciaExitosa: false,
      mostrarBotonTransferencia: true,
      transferenciaError: false,
      mensajeDeError: ""
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
    async transferir(){
      try {
        await axios.post('/api/transactions',`transactionAmount=${this.amount}&description=${this.description}&sourceAccountNumber=${this.sourceAccount}&destinationAccountNumber=${this.arrivalAccount}` ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        console.log("succesful transaction")
        this.mostrarBotonTransferencia = false
        this.mostrarDatos = false
        this.transferenciaExitosa = true
      } catch (error) {
        console.log(error.response.data)
        this.mostrarBotonTransferencia = false
        this.mostrarDatos = false
        this.transferenciaError = true
        this.mensajeDeError = error.response.data
      }
    },
    cerrar(){
      if(this.transferenciaExitosa == true){
        location.reload()
      }else{
        this.mostrarDatos= true
        this.transferenciaExitosa= false
        this.mostrarBotonTransferencia= true
        this.transferenciaError= false
        this.mensajeDeError= ""
      }
    },
    transfer(){
      $('#staticBackdrop').modal('show'); // abrir
    },
    clearForm(){
      document.querySelector("form").reset();
    }

  },
  computed:{
    
  }

}).mount('#app')