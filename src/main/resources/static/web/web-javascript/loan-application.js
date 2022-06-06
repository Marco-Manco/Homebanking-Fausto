const urlApi = `http://localhost:8080/api/loans`
Vue.createApp({
  data() {
    return {
      loans: [],
      loanType: "",
      payments: "",
      selectedLoan: {},
    }
  },

  created() {
    this.getLoansFromApi(urlApi)
  },

  methods: {
    async getLoansFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.loans = data
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
  },
  computed:{
    
  }

}).mount('#app')