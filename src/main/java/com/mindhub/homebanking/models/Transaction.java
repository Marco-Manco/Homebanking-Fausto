package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.TransactionType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    private boolean enabled;
    private Double remainingBalance;

    public Transaction(){}
    public Transaction(TransactionType type, double amount, String description, LocalDateTime date, Double remainingBalance) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.enabled = true;
        this.remainingBalance = remainingBalance;
    }
}
