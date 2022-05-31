package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getRandomAccountNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        //no estoy seguro si este primer if ese necesario en este contexto, investigar!
        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(currentClient.getAccounts().size()>2){
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account(getRandomAccountNumber(accountRepository), LocalDateTime.now(),0);
        currentClient.addAccount(newAccount);
        clientRepository.save(currentClient);
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccounts(Authentication authentication){
        return (new ClientDTO(clientRepository.findByEmail(authentication.getName()))).getAccounts();
    }

}
