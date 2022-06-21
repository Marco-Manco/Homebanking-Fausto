
Vue.createApp({

  data() {
    return {
      cardNumber: "",
      cvv: "",
      paymentAmount: "",
      description: ""
    }
  },

  created() {
  
  },

  methods: {

    async makeAPayment(){
      try {
        await axios.post('/api/cards/payment',
          {
            cardNumber: this.cardNumber,
            cardCvv: this.cvv,
            paymentAmount: this.paymentAmount,
            description: this.description
          },
          {headers:{'content-type':'application/json'}})
        console.log("successfully payment")
      } catch (error) {
        console.log(error.response.data)

      }
    },
    
    logOutClient(){
      axios.post('/api/logout').then(response => console.log('signed out!!!'))
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })//borrar codigo repetido luego
    },

  },

  computed:{
    
  }

}).mount('#app')