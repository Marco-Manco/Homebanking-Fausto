package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Data
public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<AccountDTO> accounts = new HashSet<>();
    private List<ClientLoanDTO> credits = new ArrayList<>();
    private Set<CardDTO> cards = new LinkedHashSet<>();

    public ClientDTO(){}

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(toSet());
        this.credits = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(toList());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(toSet());
        this.password = client.getPassword();
    }
}
