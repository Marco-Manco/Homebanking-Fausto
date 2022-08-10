package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.AccountType;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.ColorType;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, LoanRepository loanRepository) {
		return (args) -> {

			//Loans
			Loan loan1 = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60),30F);
			Loan loan2 = new Loan("Personal", 100000, List.of(6,12,24), 25F);
			Loan loan3 = new Loan("Automotriz", 300000, List.of(6,12,24,36),20F);
			loanRepository.saveAll(List.of(loan1,loan2,loan3));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000, AccountType.AHORRO);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT,2500,"others", LocalDateTime.now().minusDays(10),3800D);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT,1500,"gas", LocalDateTime.now().minusDays(8),5300D);

			Transaction transaction3 = new Transaction(TransactionType.DEBIT,100,"others", LocalDateTime.now().minusDays(5),5200D);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT,100,"others", LocalDateTime.now().minusDays(2),5100D);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT,100,"others", LocalDateTime.now().minusDays(1),5000D);
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account1.addTransaction(transaction4);
			account1.addTransaction(transaction5);

			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, AccountType.CORRIENTE);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT,1500,"internet", LocalDateTime.now(),6500D);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT,1000,"water", LocalDateTime.now(),7500D);
			account2.addTransaction(transaction6);
			account2.addTransaction(transaction7);

			Client client1 = new Client("Melba","Morel","melba@mindhub.com",passwordEncoder.encode("Melbamorel123"));

			//client1's cards
			Card card1 = new Card("Melba Morel", CardType.DEBIT, ColorType.GOLD,"1010202030304040",123,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card2 = new Card("Melba Morel",CardType.CREDIT,ColorType.TITANIUM,"1111222233334444",789,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card expiredCard = new Card("Melba Morel",CardType.CREDIT,ColorType.TITANIUM,"1111222233334400",111,LocalDateTime.now().minusYears(5),LocalDateTime.now());
			ClientLoan clientLoan1 = new ClientLoan(client1, loan1,400000,60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2,50000,12);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client1.addCard(card1);
			client1.addCard(card2);
			client1.addCard(expiredCard);

			clientRepository.save(client1);

			Client admin = new Client( "admin", "admin", "admin@admin",passwordEncoder.encode("admin"));
			clientRepository.save(admin);
		};
	}
}
