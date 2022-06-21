package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountById(Authentication authentication, @PathVariable Long id){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        AccountDTO account = accountService.getAccountDtoById(id);
        if(account==null){
            return null;
        }
        if(!currentClient.containsAccount(account.getNumber())){
            return null;
        }
        return accountService.getAccountDtoById(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        //no estoy seguro si este primer if ese necesario en este contexto, investigar!
        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(accountType == null){
            return new ResponseEntity<>("The type of account supplied is not valid", HttpStatus.FORBIDDEN);
        }
        if(accountService.getCurrentClientAccountsDTO(authentication).size() > 2){
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }

        accountService.createAccount(authentication, currentClient, accountType);
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication){
        return accountService.getCurrentClientAccountsDTO(authentication);
    }

    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String accountNumber){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(accountService.getCurrentClientAccountsDTO(authentication).size()<2){
            return new ResponseEntity<>("You must have unless one account", HttpStatus.FORBIDDEN);
        }
        if(!currentClient.containsAccount(accountNumber)){
            return new ResponseEntity<>("You cannot delete an account that is not yours", HttpStatus.FORBIDDEN);
        }
        if(accountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        accountService.deleteAccount(authentication, accountNumber);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    }

}
