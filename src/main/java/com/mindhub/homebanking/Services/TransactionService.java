package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    void createTransaction(Account sourceAccount, Account destinationAccount, String transactionAmount, String description);
    void deleteTransactionsForAccount(Account account);
    Set<TransactionDTO> getAccountTransactionsBetweenDates(Account account, LocalDateTime start, LocalDateTime end);
    Set<TransactionDTO> getAccountTransactions(Account account);
}
