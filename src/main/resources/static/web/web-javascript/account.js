const urlParams = new URLSearchParams(window.location.search);
const accountId = urlParams.get('id');
const urlApi = `/api/accounts/${accountId}`
var specialElementHandlers = {
  '.no-export':function(element,renderer){
    return true
  }
}

Vue.createApp({

  data() {
    return {
      account: {},
      transactions: [],
      fromDate:"",
      thruDate:""
    }
  },

  created() {
    this.getAccountFromApi(urlApi)
    this.getTransactions(accountId)
  },

  methods: {

    async getAccountFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.account = data
      } catch (error) {
        console.error(error)
      } 
    },

    async getTransactions(accountId){
      try {
        const {data} = await axios.get(`/api/clients/current/transactions?accountId=${accountId}`)
        this.transactions = data
      } catch (error) {
        console.error(error)
      } 
    },

    classObject(transactionType) {
      return {
        'text-danger': transactionType == 'DEBIT',
        'text-success': transactionType == 'CREDIT',
        'table-danger': transactionType == 'DEBIT',
        'table-success': transactionType == 'CREDIT',
      }
    },

    logOutClient(){
      axios.post('/api/logout').then(response => console.log('signed out!!!'))
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },

    formatDate(localDateTime){
      let [date,hour] = localDateTime.split("T")
      let [formatedHour] = hour.split(".")
      return `${date} ${formatedHour}`
    },

    async filterTransactions(){
      try {
        const {data} = await axios.get(`/api/clients/current/transactions?accountId=${accountId}&start=${this.fromDate + 'T00:00:00'}&end=${this.thruDate + 'T00:00:00'}`)
        this.transactions = data
      } catch (error) {
        console.error(error)
      } 
    },

    print(){
      let doc = new jsPDF()
      doc.autoTable({
        head:[['Type','Amount','Description','Date','Remaining balance']],
        // body:[['name',15,'Country']]
        body: this.transactions.map(transaction =>{
          let amount = ""
          if(transaction.type == "DEBIT"){
            amount = "-$" + transaction.amount
          }else{
            amount = "$" + transaction.amount
          }
          return [transaction.type, amount, transaction.description, this.formatDate(transaction.date), "$" + transaction.remainingBalance]
        })
        

      })
      doc.save("table.pdf")
    }
    
  },

  computed:{
    buttonIsDisabled(){
      if(this.fromDate == "" || this.thruDate == ""){
        return true
      }
      return false
    }
  }

}).mount('#app')