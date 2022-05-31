package com.mindhub.homebanking.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String cardHolder;
    private CardType type;
    private ColorType color;
    private String number; //agregar metodos de validacion luego!!
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card(){}
    public Card(String cardHolder, CardType type, ColorType color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }
}
