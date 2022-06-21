package com.mindhub.homebanking.models;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //probando si en el linkedhash no se pierde el orden en q se agregan las cards
    private Set<Card> cards = new HashSet<>();
    private boolean enabled;

    public Client(){}
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = true;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    //metodos utiles
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }
    public boolean containsAccount(String accountNumber){
        //este if esta para q no se rompa cuando accouts este vacio, revisar validaciones luego, no me gusta esto aca
        if(this.accounts.size() == 0){
            return false;
        }
        return this.accounts.stream().map(Account::getNumber).collect(Collectors.toList()).contains(accountNumber);
    }
}
