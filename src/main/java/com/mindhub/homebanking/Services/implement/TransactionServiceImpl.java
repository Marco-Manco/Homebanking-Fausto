package com.mindhub.homebanking.Services.implement;

import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void createTransaction(Account sourceAccount, Account destinationAccount, String transactionAmount, String description) {
        Transaction originAccountTransaction = new Transaction(TransactionType.DEBIT, Double.parseDouble(transactionAmount),
                description + " " + destinationAccount.getNumber(), LocalDateTime.now(),
                sourceAccount.getBalance() - Double.parseDouble(transactionAmount));
        Transaction destinationAccountTransaction = new Transaction(TransactionType.CREDIT, Double.parseDouble(transactionAmount),
                description + " " + sourceAccount.getNumber(),LocalDateTime.now(),
                destinationAccount.getBalance() + Double.parseDouble(transactionAmount));

        sourceAccount.addTransaction(originAccountTransaction);
        sourceAccount.setBalance(sourceAccount.getBalance() - Double.parseDouble(transactionAmount));

        destinationAccount.addTransaction(destinationAccountTransaction);
        destinationAccount.setBalance(destinationAccount.getBalance() + Double.parseDouble(transactionAmount));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

    @Override
    public void deleteTransactionsForAccount(Account account) {
        account.getTransactions().forEach(transaction -> {
            transaction.setEnabled(false);
            transactionRepository.save(transaction);
        });
    }



    @Override
    public Set<TransactionDTO> getAccountTransactions(Account account) {
        return account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<TransactionDTO> getAccountTransactionsBetweenDates(Account account, LocalDateTime start, LocalDateTime end){
        Set<Transaction> allAccountTransactions = account.getTransactions();
        List<Transaction> transactions = transactionRepository.findByDateBetween(start, end);
        if(allAccountTransactions.size() == 0){
            return null;
        }
        if(transactions.size() != 0){
            return transactions.stream().filter(allAccountTransactions::contains).map(TransactionDTO::new).collect(Collectors.toSet());
        }
        return null;
    }

}
