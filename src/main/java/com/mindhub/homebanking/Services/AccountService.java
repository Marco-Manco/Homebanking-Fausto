package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.enums.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface AccountService {
    List<Account> getAccounts();
    Account getEnabledAccountById(Long id);
    void create(Authentication authentication, Client currentClient, AccountType accountType);
    Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication);
    Account getByNumber(String accountNumber);
    void save(Account account);
    void delete(String accountNumber);

    Account getCurrentClientAccountWithSufficientBalance(Authentication authentication, Double paymentAmount);
}
