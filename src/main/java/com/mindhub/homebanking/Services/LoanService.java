package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreationApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanVisibleToTheCLientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanVisibleToTheCLientDTO> getLoansDTO();
    Loan getLoanById(Long id);
    void createLoan(Client currentClient, Loan loan, Account destinationAccount, LoanApplicationDTO loanApplicationDTO);

    void createNewTypeOfLoan(LoanCreationApplicationDTO loanCreationApplicationDTO);
}
