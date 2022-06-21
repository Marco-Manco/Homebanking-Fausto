package com.mindhub.homebanking.dtos;

import lombok.Data;

@Data
public class CardPaymentDTO {
//    número, código de seguridad, monto y descripción

    private String cardNumber;
    private Integer cardCvv;
    private Double paymentAmount;
    private String description;

    public CardPaymentDTO(){}
    public boolean isSomePropertyNull(){
        return (this.cardNumber == null || this.cardCvv == null
                || this.paymentAmount == null || this.description == null);
    }
}
