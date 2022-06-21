package com.mindhub.homebanking.Services.implement;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getRandomAccountNumber;
import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;
    @Override
    public List<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public AccountDTO getAccountDtoById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if(account != null){
            return account.isEnabled() ? new AccountDTO(account) : null;
        }
        return null;
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }


    @Override
    public void createAccount(Authentication authentication, Client currentClient, AccountType accountType) {
        Account newAccount = new Account(getRandomAccountNumber(accountRepository), LocalDateTime.now(),0, accountType);
        currentClient.addAccount(newAccount);
        clientService.saveClient(currentClient);
    }

    @Override
    public Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication) {
        Set<Account> clientAccounts = clientService.getClientByEmail(authentication.getName()).getAccounts();
        return clientAccounts.stream().filter(Account::isEnabled).map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        return account.isEnabled() ? account : null;
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Authentication authentication, String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        if (account != null) {
            account.setEnabled(false);
            transactionService.deleteTransactionsForAccount(account);
            accountRepository.save(account);
        }

    }

    @Override
    public Account getCurrentClientAccountWithSufficientBalance(Authentication authentication, Double paymentAmount) {
        AccountDTO accountWithSufficientBalanceDTO = this.getCurrentClientAccountsDTO(authentication).stream()
                .filter(accountDTO -> accountDTO.getBalance() >= paymentAmount).findFirst().orElse(null);
        if(accountWithSufficientBalanceDTO == null){
            return null;
        }
        return accountRepository.findById(accountWithSufficientBalanceDTO.getId()).orElse(null);
    }
}
