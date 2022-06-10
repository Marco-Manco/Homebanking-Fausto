const urlParams = new URLSearchParams(window.location.search);
const accountId = urlParams.get('id');
const urlApi = `http://localhost:8080/api/clients/current/accounts`
Vue.createApp({

  data() {
    return {
      account: {},
    }
  },

  created() {
    this.getAccountFromApi(urlApi)
  },

  methods: {

    async getAccountFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.account = data.filter(account => account.id == accountId)[0]
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
  },

  computed:{
    
  }

}).mount('#app')