package com.mindhub.homebanking.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    private double amount;
    private int payments;

    public ClientLoan(){}
    public ClientLoan(Client client, Loan loan, double amount, int payments){
        this.client = client;
        this.loan = loan;
        this.amount = amount;
        this.payments = payments;
    }

}
