package com.mindhub.homebanking.Services.implement;

import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreationApplicationDTO;
import com.mindhub.homebanking.dtos.LoanVisibleToTheCLientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public List<LoanVisibleToTheCLientDTO> getLoansDTO() {
        return loanRepository.findAll().stream().map(LoanVisibleToTheCLientDTO::new).collect(Collectors.toList());
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void createLoan(Client currentClient, Loan loan, Account destinationAccount, LoanApplicationDTO loanApplicationDTO) {
        ClientLoan clientLoan = new ClientLoan(currentClient, loan,
                (1 + loan.getInterestPercentage()/100) * loanApplicationDTO.getAmount(),
                loanApplicationDTO.getPayments());
        currentClient.addClientLoan(clientLoan);
        clientRepository.save(currentClient);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),
                loan.getName() + " Loan approved", LocalDateTime.now(),
                destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        destinationAccount.addTransaction(transaction);
        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(destinationAccount);
    }

    @Override
    public void createNewTypeOfLoan(LoanCreationApplicationDTO loanCreationApplicationDTO) {
        Loan loan = new Loan(loanCreationApplicationDTO.getName(), loanCreationApplicationDTO. getMaxAmount(),
                loanCreationApplicationDTO.getPayments(), loanCreationApplicationDTO.getInterestPercentage());
        loanRepository.save(loan);
    }
}
