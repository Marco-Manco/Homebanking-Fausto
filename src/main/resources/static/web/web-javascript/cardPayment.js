
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
          Swal.fire('Payment made', '', 'success')
          .then(() =>{
            location.reload()
          })
        console.log("successfully payment")
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: `${error.response.data}`,
        })
      }
    },
    
    logOutClient(){
      axios.post('/api/logout').then(response => console.log('signed out!!!'))
        .then(()=>{
          let [url] = location.href.split("web")
          location.href = `${url}web/index.html`
        })
    },
    openPaySwal(){
      Swal.fire({
        title: 'Are you sure to make the payment?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: 'No',
        confirmButtonText: 'Yes'
      }).then((result) => {
        if (result.isConfirmed){
          this.makeAPayment()
        }
      })
    },

  },

  computed:{
    
  }

}).mount('#app')