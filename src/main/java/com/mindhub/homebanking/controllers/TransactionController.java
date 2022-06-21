package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam String transactionAmount, @RequestParam String description,
                                             @RequestParam String sourceAccountNumber, @RequestParam String destinationAccountNumber) {

        Client currentClient = clientService.getClientByEmail(authentication.getName());
        Account sourceAccount = accountService.getAccountByNumber(sourceAccountNumber);
        Account destinationAccount = accountService.getAccountByNumber(destinationAccountNumber);

        if(transactionAmount.isEmpty() || description.isEmpty() || sourceAccountNumber.isEmpty() || destinationAccountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(sourceAccountNumber.equals(destinationAccountNumber)){
            return new ResponseEntity<>("Same account numbers", HttpStatus.FORBIDDEN);
        }
        if(sourceAccount == null){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }
        if(Integer.parseInt(transactionAmount) <= 0){
            return new ResponseEntity<>("The amount can not be negative",HttpStatus.FORBIDDEN);
        }
        if(!currentClient.containsAccount(sourceAccountNumber)){
            return new ResponseEntity<>("The account does not belong to you",HttpStatus.FORBIDDEN);
        }
        if(destinationAccount == null){
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if(sourceAccount.getBalance() < Double.parseDouble(transactionAmount)){
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        transactionService.createTransaction(sourceAccount, destinationAccount, transactionAmount, description);
        return new ResponseEntity<>("Succesful transaction", HttpStatus.ACCEPTED);
    }
    @GetMapping("/clients/current/transactions")
    public ResponseEntity<?> getCurrentClientTransactions(Authentication authentication, @RequestParam Long accountId,
                                                            @RequestParam(required = false) String start,
                                                            @RequestParam(required = false) String end){
        Client currenClient = clientService.getClientByEmail(authentication.getName());

        if(accountId == null){
            return new ResponseEntity<>("Missing data (accountId)", HttpStatus.FORBIDDEN);
        }
        Account account = accountService.getAccountById(accountId);
        if(!account.isEnabled()){
            return new ResponseEntity<>("The account have been deleted", HttpStatus.FORBIDDEN);
        }
        if(!currenClient.getAccounts().contains(account)){
            return new ResponseEntity<>("The account is not yours", HttpStatus.FORBIDDEN);
        }
        if(start == null && end == null){
            return new ResponseEntity<>(transactionService.getAccountTransactions(account), HttpStatus.OK);
        }
        LocalDateTime localDateStart = LocalDateTime.parse(start);
        LocalDateTime localDateEnd = LocalDateTime.parse(end);
        return new ResponseEntity<>(transactionService.getAccountTransactionsBetweenDates(account, localDateStart, localDateEnd),HttpStatus.OK);
    }
}
