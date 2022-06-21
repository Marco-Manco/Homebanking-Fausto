const urlApi = 'http://localhost:8080/api/clients/current/cards'

Vue.createApp({

  data() {
    return {
      cards: [],
      creditCards: [],
      debitCards: []
    }
  },

  created() {
    this.getCardsFromApi(urlApi)
  },

  methods: {
    //cambiar luego para q reciba id
    async getCardsFromApi(url){
      try {
        const {data} = await axios.get(url)
        this.cards = data
        this.creditCards = this.cards.filter(card => card.type == "CREDIT")
        this.debitCards = this.cards.filter(card => card.type == "DEBIT")
      } catch (error) {
        console.error(error)
      } 
    },

    getMonthYear(dateHour){
      let [date] = dateHour.split("T")
      let [year,month]= date.split("-")
      return `${month}/${year.slice(-2)}`
    },

    getBackgroundColorClass(color) {
      return {
        'gold': color == 'GOLD',
        'silver': color == 'SILVER',
        'titanium': color == 'TITANIUM',
      }
    },
    logOutClient(){
      axios.post('/api/logout').then(response => console.log('signed out!!!'))
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },
    formatCardNumber(number){
      return  number.match(/.{1,4}/g).join(" ");
    },

    async deleteCard(cardId){
      try {
        await axios.patch(`http://localhost:8080/api/clients/current/cards?cardId=${cardId}`)
        location.reload()
        console.log("Card successfull deleted")
      } catch (error) {
        console.log(error.response.data)
      }
    },
    isExpired(expirationDate){
      let actualDate = new Date()
      let [date] = actualDate.toISOString().split("T")
      let [thruDate] = expirationDate.split("T")
      return date == thruDate
    }
    
  },

  computed:{
    
  }

}).mount('#app')