package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
			//

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000, AccountType.AHORRO);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT,2500,"others", LocalDateTime.now().minusDays(10),3800D);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT,1500,"gas", LocalDateTime.now().minusDays(8),5300D);

			Transaction transaction12 = new Transaction(TransactionType.DEBIT,100,"others", LocalDateTime.now().minusDays(5),5200D);
			Transaction transaction13 = new Transaction(TransactionType.DEBIT,100,"others", LocalDateTime.now().minusDays(2),5100D);
			Transaction transaction14 = new Transaction(TransactionType.DEBIT,100,"others", LocalDateTime.now().minusDays(1),5000D);
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction12);
			account1.addTransaction(transaction13);
			account1.addTransaction(transaction14);

			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, AccountType.CORRIENTE);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT,1500,"internet", LocalDateTime.now(),6500D);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,1000,"water", LocalDateTime.now(),7500D);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);

			Client client1 = new Client("Melba","Morel","melba@mindhub.com",passwordEncoder.encode("Melbamorel123"));
			//client1's cards
			Card card1 = new Card("Melba Morel",CardType.DEBIT,ColorType.GOLD,"1010202030304040",123,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
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

			//client2
			Account account3 = new Account("VIN003", LocalDateTime.now(), 8000, AccountType.AHORRO);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT,1500,"light", LocalDateTime.now(),3500D);
			Transaction transaction6 = new Transaction(TransactionType.CREDIT,2500,"shoes", LocalDateTime.now(),6000D);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT,2000,"tv", LocalDateTime.now(),8000D);

			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account3.addTransaction(transaction7);

			Account account4 = new Account("VIN004", LocalDateTime.now().plusDays(1), 7500, AccountType.AHORRO);
			Transaction transaction8 = new Transaction(TransactionType.DEBIT,500,"others", LocalDateTime.now(),7500D);
			account4.addTransaction(transaction8);

			Client client2 = new Client("Pedro","Diaz","peddro-diaz@mindhub.com",passwordEncoder.encode("Pedrodiaz123"));
			//client2's cards
			Card card3 = new Card("Pedro Diaz",CardType.CREDIT, ColorType.SILVER,"9999888877776666",456,LocalDateTime.now(),LocalDateTime.now().plusYears(5));

			ClientLoan clientLoan3 = new ClientLoan(client2, loan2,100000,24);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3,200000,36);

			client2.addAccount(account3);
			client2.addAccount(account4);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			client2.addCard(card3);

			clientRepository.save(client2);

			//client3 without loans
			Account account5 = new Account("VIN005", LocalDateTime.now(), 8000,AccountType.AHORRO);
			Transaction transaction9 = new Transaction(TransactionType.DEBIT,4000,"shoes", LocalDateTime.now(), 8000D);

			account5.addTransaction(transaction9);

			Account account6 = new Account("VIN006", LocalDateTime.now().plusDays(1), 7500, AccountType.AHORRO);
			Transaction transaction10 = new Transaction(TransactionType.DEBIT,2500,"water", LocalDateTime.now(),1500D);
			Transaction transaction11 = new Transaction(TransactionType.CREDIT,6000,"jacket", LocalDateTime.now(),7500D);

			account6.addTransaction(transaction10);
			account6.addTransaction(transaction11);

			Client client3 = new Client("Juana","Mendes","juana-mendes@mindhub.com",passwordEncoder.encode("Juanamendes123"));

			client3.addAccount(account5);
			client3.addAccount(account6);

			clientRepository.save(client3);

			Client admin = new Client( "admin", "admin", "admin@admin",passwordEncoder.encode("admin"));
			clientRepository.save(admin);
		};
	}
}
