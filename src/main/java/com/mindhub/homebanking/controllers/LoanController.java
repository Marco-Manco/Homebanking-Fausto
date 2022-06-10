package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanVisibleToTheCLientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/loans")
    public List<LoanVisibleToTheCLientDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanVisibleToTheCLientDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanAplicationDTO){


//        Se debe actualizar la cuenta de destino sumando el monto solicitado.

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanAplicationDTO.getLoanId()).orElse(null);
        Account destinationAccount = accountRepository.findByNumber(loanAplicationDTO.getDestinationAccountNumber());

        if(loanAplicationDTO.isSomePropertyNull()){
            return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
        }
        if(loanAplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Amount can not be 0", HttpStatus.FORBIDDEN);
        }
        if(loanAplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Payments can not be 0", HttpStatus.FORBIDDEN);
        }
        if(loan == null){
            return new ResponseEntity<>("Loan does not exist",HttpStatus.FORBIDDEN);
        }
        if(loanAplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The requested amount of the loan exceeds the maximum amount", HttpStatus.FORBIDDEN);
        }
        if(!loan.getPayments().contains(loanAplicationDTO.getPayments())){
            return new ResponseEntity<>("The requested loan does not contain the amount of payments indicated",
                    HttpStatus.FORBIDDEN);
        }
        if(destinationAccount == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!currentClient.containsAccount(loanAplicationDTO.getDestinationAccountNumber())) {
            return new ResponseEntity<>("The destination account does not belong to you", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(currentClient,loan,loanAplicationDTO.getAmount() + loanAplicationDTO.getAmount()*0.2,
                loanAplicationDTO.getPayments());
        currentClient.addClientLoan(clientLoan);
        clientRepository.save(currentClient);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanAplicationDTO.getAmount(),
                loan.getName() + " Loan approved", LocalDateTime.now());
        destinationAccount.addTransaction(transaction);
        destinationAccount.setBalance(destinationAccount.getBalance() + loanAplicationDTO.getAmount());
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Loan approved successfully", HttpStatus.CREATED);
    }
}
