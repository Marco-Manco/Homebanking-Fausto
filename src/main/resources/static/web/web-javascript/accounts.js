const urlApi = 'http://localhost:8080/api/clients/current'
Vue.createApp({

  data() {
    return {
      client: {},  
      // canAddMoreAccounts: true,
    }
  },

  created() {
    this.getClientFromApi(urlApi)
  },

  methods: {

    //cambiar luego para q reciba id
    async getClientFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.client = data
      } catch (error) {
        console.error(error)
      } 
    },
    goToTransactionsForAccount(id){
      let [url] = location.href.split("web")
      location.href = `${url}web/account.html?id=${id}`
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
        await axios.post('/api/clients/current/accounts' ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        //mostrar algun mensaje o algo de q se creo la cuenta en el html
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
    }
  },
  computed:{
    
  }

}).mount('#app')