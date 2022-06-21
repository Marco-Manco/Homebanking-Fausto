package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface CardService {
    Set<CardDTO> getCurrentClientCardsDTO(Authentication authentication);
    void createCard(Client currentClient, String cardType, String colorType);
    void deleteCard(Long cardId);

    Card getCardById(Long cardId);
    void makePayment(Account account, CardPaymentDTO cardPaymentDTO);
    Card getCardByNumber(String number);
}
