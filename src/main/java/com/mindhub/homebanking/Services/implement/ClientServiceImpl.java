package com.mindhub.homebanking.Services.implement;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.mindhub.homebanking.utils.Utils.getRandomAccountNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ClientDTO getCurrentClientDTO(Authentication authentication){
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Set<Account> accounts = clientRepository.findByEmail(authentication.getName()).getAccounts();

        ClientDTO clientDTO = new ClientDTO(currentClient);
        Set<AccountDTO> enabledAccountDTO = accounts.stream().filter(Account::isEnabled).map(AccountDTO::new).collect(toSet());
        clientDTO.setAccounts(enabledAccountDTO);
        return clientDTO;
    }
    @Override
    public List<ClientDTO> getClientsDTO(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @Override
    public ClientDTO getClientDtoById(Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    @Override
    public Client getClientByEmail(String email){
        return clientRepository.findByEmail(email);
    }
    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    @Override
    public void registerClient(String firstName, String lastName, String email, String password){
        Account newAccount = new Account(getRandomAccountNumber(accountRepository), LocalDateTime.now(),0, AccountType.AHORRO);
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        client.addAccount(newAccount);
        this.saveClient(client);
    }
}
