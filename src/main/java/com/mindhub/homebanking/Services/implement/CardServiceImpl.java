package com.mindhub.homebanking.Services.implement;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

import static com.mindhub.homebanking.utils.Utils.getRandomCardNumber;
import static java.util.stream.Collectors.toSet;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    AccountService accountService;

    @Override
    public Set<CardDTO> getCurrentClientCardsDTO(Authentication authentication) {
        Set<Card> clientCards = clientRepository.findByEmail(authentication.getName()).getCards();
        return clientCards.stream().filter(Card::isEnabled).map(CardDTO::new).collect(toSet());
    }

    @Override
    public void createCard(Client currentClient, String cardType, String colorType) {
        Card newCard = new Card(currentClient.getFullName(), CardType.valueOf(cardType), ColorType.valueOf(colorType),
                getRandomCardNumber(cardRepository), (int) (100 + Math.random()*900), LocalDateTime.now(),
                LocalDateTime.now().plusYears(5));

        currentClient.addCard(newCard);
        clientRepository.save(currentClient);
    }

    @Override
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);
        if (card!=null){
            card.setEnabled(false);
            cardRepository.save(card);
        }
    }

    @Override
    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId).orElse(null);
    }

    @Override
    public void makePayment(Account account, CardPaymentDTO cardPaymentDTO) {

        Transaction transaction = new Transaction(TransactionType.DEBIT, cardPaymentDTO.getPaymentAmount(),
                cardPaymentDTO.getDescription(),LocalDateTime.now(),
                account.getBalance() - cardPaymentDTO.getPaymentAmount());
        account.setBalance(account.getBalance() - cardPaymentDTO.getPaymentAmount());
        account.addTransaction(transaction);
        accountService.save(account);
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
}
