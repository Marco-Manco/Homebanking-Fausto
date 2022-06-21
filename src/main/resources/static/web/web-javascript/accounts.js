


const urlApi = '/api/clients/current'
Vue.createApp({

  data() {
    return {
      client: {},  
      lastFiveTransactions: [],
      accountType: ""
    }
  },

  created() {
    this.getDataFromApi(urlApi)
  },

  methods: {

    //cambiar luego para q reciba id
    async getDataFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.client = data
        this.lastFiveTransactions = this.getLastFiveTransactions(data.accounts)
      } catch (error) {
        console.log(error.response.data)
      } 
    },
    getLinkForTransactions(accountId){
      return `./account.html?id=${accountId}`
    },
    logOutClient(){
      axios.post('/api/logout')
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },
    async addAccount(){
      try {
        await axios.post('/api/clients/current/accounts' ,`accountType=${this.accountType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        location.reload()
      } catch (error) {
        console.log("no se pudo agregar una cuenta nueva")
        console.error(error)
      }
    },
    formatDate(localDateTime){
      let [date,hour] = localDateTime.split("T")
      let [formatedHour] = hour.split(".")
      return `${date} ${formatedHour}`
    },
    getTotalBalance(){
      return this.client.accounts?.map(account => account.balance).reduce((a,b)=>a+b,0)
    },

    getLastFiveTransactions(accounts){
      let transactions = this.getTransactionsForAccounts(accounts)
      return transactions.length <=5 ? transactions : transactions.slice(0,5)
    },
    getTransactionsForAccounts(accounts){
      let transactionsArray = []
      accounts.forEach(account => account.transactions.length != 0 ? transactionsArray = transactionsArray.concat(account.transactions) : "")
      return transactionsArray.sort((prevTransaction, nextTransaction)=> nextTransaction.id - prevTransaction.id)
    },
    async deleteAccount(accountNumber){
      try {
        await axios.patch(`/api/clients/current/accounts?accountNumber=${accountNumber}`)
        location.reload()
      } catch (error) {
        console.log(error.response.data)
      }
    },
    openModal(){
      $('#staticBackdrop').modal('show'); // abrir
    },
  },
  computed:{
    
  }

}).mount('#app')