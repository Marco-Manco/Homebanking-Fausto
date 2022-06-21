const loanEndpoint = `/api/loans`
const accountsEndpoint = `/api/clients/current/accounts`
Vue.createApp({
  data() {
    return {
      loans: [],
      accounts:[],
      loanId: "",
      payments: "",
      amount: "",
      destinationAccount: "",

      successfullLoan: false,
      errorMessage: "",
      showConfirmationMessage: true
    }
  },

  async created() {
    // this.getLoansFromApi(urlApi)
    this.loans = await this.getDataFromApi(loanEndpoint)
    this.accounts = await this.getDataFromApi(accountsEndpoint)
  },

  methods: {
    async getDataFromApi(url){
      try {
        const {data} = await axios.get(url)
        return data
      } catch (error) {
        console(error.response.data)
      } 
    },
    logOutClient(){
      axios.post('/api/logout')
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },
    getLoanFromId(){
      return this.loans?.find(loan => loan.id == this.loanId)
    },
    showConfirmationModal(){
      $('#staticBackdrop').modal('show'); // abrir
    },
    async applyForLoan(){
      try {
        await axios.post('/api/loans',
          {
            loanId: this.loanId,
            amount: this.amount,
            payments: this.payments,
            destinationAccountNumber: this.destinationAccount
          },
          {headers:{'content-type':'application/json'}})
        this.successfullLoan = true
        this.showConfirmationMessage = false

      } catch (error) {
        this.errorMessage = error.response.data
        console.log(error.response.data)
      } 
    },
    resetModalValues(){
      this.successfullLoan = false,
      this.errorMessage = "",
      this.showConfirmationMessage = true
    },
    
  },
  
  computed:{
    
  }

}).mount('#app')