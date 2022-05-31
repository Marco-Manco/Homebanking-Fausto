package com.mindhub.homebanking.controllers;

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

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam String transactionAmount, @RequestParam String description,
                                             @RequestParam String sourceAccountNumber, @RequestParam String destinationAccountNumber) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        //no estoy seguro si este primer if ese necesario en este contexto, investigar!
        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(transactionAmount.isEmpty() || description.isEmpty() || sourceAccountNumber.isEmpty() || destinationAccountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(sourceAccountNumber.equals(destinationAccountNumber)){
            return new ResponseEntity<>("Same account numbers", HttpStatus.FORBIDDEN);
        }
        if(sourceAccount == null){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
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

        Transaction originAccountTransaction = new Transaction(TransactionType.DEBIT,-1*Double.parseDouble(transactionAmount),
                description + " " + destinationAccountNumber,LocalDateTime.now());
        Transaction destinationAccountTransaction = new Transaction(TransactionType.CREDIT, Double.parseDouble(transactionAmount),
                description + " " + sourceAccountNumber,LocalDateTime.now());

        sourceAccount.addTransaction(originAccountTransaction);
        sourceAccount.setBalance(sourceAccount.getBalance() - Double.parseDouble(transactionAmount));

        destinationAccount.addTransaction(destinationAccountTransaction);
        destinationAccount.setBalance(destinationAccount.getBalance() + Double.parseDouble(transactionAmount));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        //entra al if cuando es una transaccion entre cuentas propias, al else cuando es una transaccion a la cuenta de un 3ero
//        if(currentClient.getId() == destinationAccount.getClient().getId()){
//
//        }
        return new ResponseEntity<>("Succesful transaction", HttpStatus.ACCEPTED);
    }
}
