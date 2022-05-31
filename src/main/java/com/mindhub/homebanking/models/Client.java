package com.mindhub.homebanking.models;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
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

    public Client(){}
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }
    public Long getId(){
        return this.id;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    //hace practicamente lo mismo q getClientLoans: revisar esto luego!
//    public List<ClientLoan> getLoans() {
//        return new ArrayList<>(clientLoans);
//    }

    public Set<Card> getCards() {
        return cards;
    }

    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    //metodos utiles
    public boolean containsAccount(String accountNumber){
        //este if esta para q no se rompa cuando accouts este vacio, revisar validaciones luego, no me gusta esto aca
        if(this.accounts.size() == 0){
            return false;
        }
        return this.accounts.stream().map(Account::getNumber).collect(Collectors.toList()).contains(accountNumber);
    }
}
