package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.dtos.*;
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
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/loans")
    public List<LoanVisibleToTheCLientDTO> getLoansDTO(){
        return loanService.getLoansDTO();
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanAplicationDTO){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        Loan loan = loanService.getLoanById(loanAplicationDTO.getLoanId());
        Account destinationAccount = accountService.getAccountByNumber(loanAplicationDTO.getDestinationAccountNumber());

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

        loanService.createLoan(currentClient,loan, destinationAccount, loanAplicationDTO);

        return new ResponseEntity<>("Loan approved successfully", HttpStatus.CREATED);
    }
    @PostMapping("/loans/new")
    public ResponseEntity<Object> createNewTypeOfLoan(Authentication authentication,
                                                      @RequestBody LoanCreationApplicationDTO loanCreationApplicationDTO){
        Client admin = clientService.getClientByEmail(authentication.getName());
        if(loanCreationApplicationDTO.isSomePropertyNull()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(loanCreationApplicationDTO.getInterestPercentage() <= 0 ||
                loanCreationApplicationDTO.getInterestPercentage() > 100){
            return new ResponseEntity<>("Interest percentage must be between 0 and 100", HttpStatus.FORBIDDEN);
        }
        loanService.createNewTypeOfLoan(loanCreationApplicationDTO);
        return new ResponseEntity<>("New type of loan created successfully", HttpStatus.CREATED);
    }
}
