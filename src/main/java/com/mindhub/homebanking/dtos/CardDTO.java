package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CardDTO {
    private Long id;
    private String cardHolder;
    private CardType type;
    private ColorType color;
    private String number;
    private int cvv;
    //cambiar el tipo de dato string por uno mas acorde luego (month/year)!
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    public CardDTO(){}
    public CardDTO(Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }
}
