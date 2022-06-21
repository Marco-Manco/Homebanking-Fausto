package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }
    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    //CardRepository
    @Test
    public void existCardByNumber() {
        boolean existCardInBdByCardNumber = cardRepository.existsByNumber("1010202030304040");
        assertThat(existCardInBdByCardNumber, equalTo(true));

        existCardInBdByCardNumber = cardRepository.existsByNumber("does not exist this card number in bd");
        assertThat(existCardInBdByCardNumber, equalTo(false));
    }
    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    //ClientRepository
    @Test
    public void existClientByEmail() {
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        assertThat(client, is(notNullValue()));
    }
    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    //TransactionRepository
    @Test
    public void existTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(notNullValue()));
    }
    @Test
    public void existTransactionById() {
        Transaction transaction = transactionRepository.findById(1L).orElse(null);
        assertThat(transaction, is(notNullValue()));
    }

    //AccountRepository
    @Test
    public void existAccountByNumber() {
        Account account = accountRepository.findByNumber("VIN001");
        assertThat(account, is(notNullValue()));
    }
    @Test
    public void existAlreadyInDbByNumber() {
        boolean alreadyExistInDb = accountRepository.existsByNumber("VIN001");
        assertThat(alreadyExistInDb, equalTo(true));
    }

    //utils, refactorizar metodos para q no conozcan al repositorio y asi poder probarlos unitariamente
}
