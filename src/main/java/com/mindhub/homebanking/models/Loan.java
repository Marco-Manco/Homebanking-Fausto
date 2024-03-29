package com.mindhub.homebanking.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@Entity
@Getter
@Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name;
    private double maxAmount;

    @ElementCollection
    @Column(name="payment")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    private Float interestPercentage;

    public Loan(){}
    public Loan(String name, double maxAmount, List<Integer> payments, Float interestPercentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interestPercentage = interestPercentage;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }


    public List<Client> getClients(){
        return clientLoans.stream().map(ClientLoan::getClient).collect(toList());
    }
}
