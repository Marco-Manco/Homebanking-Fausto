package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface AccountService {
    List<AccountDTO> getAccountsDTO();
    AccountDTO getAccountDtoById(Long id);
    Account getById(Long id);
    void create(Authentication authentication, Client currentClient, AccountType accountType);
    Set<AccountDTO> getCurrentClientAccountsDTO(Authentication authentication);
    Account getByNumber(String accountNumber);
    void save(Account account);
    void delete(String accountNumber);

    Account getCurrentClientAccountWithSufficientBalance(Authentication authentication, Double paymentAmount);
}
