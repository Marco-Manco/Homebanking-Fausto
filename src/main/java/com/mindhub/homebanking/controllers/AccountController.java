package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.enums.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO() {
        return accountService.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getCurrentClientAccountById(Authentication authentication, @PathVariable Long id){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.getEnabledAccountById(id);

        if(account == null){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }
        if(!currentClient.containsAccount(account.getNumber())){
            return new ResponseEntity<>("The account does not belong to you", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new AccountDTO(accountService.getEnabledAccountById(id)), HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client currentClient = clientService.getClientByEmail(authentication.getName());

        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(accountType == null){
            return new ResponseEntity<>("The type of account supplied is not valid", HttpStatus.FORBIDDEN);
        }
        if(accountService.getCurrentClientAccountsDTO(authentication).size() > 2){
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }

        accountService.create(authentication, currentClient, accountType);
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication){
        return accountService.getCurrentClientAccountsDTO(authentication);
    }

    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<?> deleteAccount(Authentication authentication, @RequestParam String accountNumber){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if(accountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Account account = accountService.getByNumber(accountNumber);

        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(account == null){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }
        if(accountService.getCurrentClientAccountsDTO(authentication).size()<2){
            return new ResponseEntity<>("You must have unless one account", HttpStatus.FORBIDDEN);
        }
        if(!currentClient.containsAccount(accountNumber)){
            return new ResponseEntity<>("You cannot delete an account that is not yours", HttpStatus.FORBIDDEN);
        }
        if(!account.isEnabled()){
            return new ResponseEntity<>("The account has been already deleted", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(accountNumber).getBalance()>0){
            return new ResponseEntity<>("You can not delete an account with balance, transfer the money first", HttpStatus.FORBIDDEN);
        }
        accountService.delete(accountNumber);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    }

}
