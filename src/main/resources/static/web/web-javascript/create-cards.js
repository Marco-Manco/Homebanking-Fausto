Vue.createApp({
  data() {
    return {
      client: {},  
      cardColor: "",
      cardType: ""
    }
  },

  created() {
    
  },

  methods: {

    logOutClient(){
      axios.post('/api/logout')
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },
    async addCard(){
      try {
        await axios.post('/api/clients/current/cards',`color=${this.cardColor}&type=${this.cardType}` ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        //mostrar algun mensaje o algo de q se creo la cuenta en el html
        this.goToCards()
      } catch (error) {
        console.log("no se pudo agregar una cuenta nueva")
        console.error(error)
      }
    },
    goToCards(){
      let [url] = location.href.split("web")
      location.href = `${url}web/cards.html`
      console.log(location.href)
    },
    addColor(color){
      this.cleanCardColors()
      let cardContainer = document.querySelector(".flip-card-inner")
      switch (color) {
        case "GOLD":
          this.cleanCardColors()
          cardContainer.classList.add("gold")
          break;
        case "SILVER":
          this.cleanCardColors()
          cardContainer.classList.add("silver")
          break;
        case "TITANIUM":
          this.cleanCardColors()
          cardContainer.classList.add("titanium")
          break;
        default:
          break;
      }
      cardContainer.classList.add()
    },
    cleanCardColors(){
      let cardContainer = document.querySelector(".flip-card-inner")
      if(cardContainer.classList.contains("gold")){
        cardContainer.classList.remove("gold")
      }
      if(cardContainer.classList.contains("silver")){
        cardContainer.classList.remove("silver")
      }
      if(cardContainer.classList.contains("titanium")){
        cardContainer.classList.remove("titanium")
      }
      
    }
  },
  computed:{
    
  }

}).mount('#app')