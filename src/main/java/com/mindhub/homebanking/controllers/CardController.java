package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getRandomCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam String color, @RequestParam String type) {
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        //no estoy seguro si este primer if ese necesario en este contexto, investigar!
        if (currentClient == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!(Arrays.stream(CardType.values()).map(Enum::name).collect(Collectors.toList()).contains(type))){
            return new ResponseEntity<>("Invalid card type", HttpStatus.FORBIDDEN);
        }
        if(!(Arrays.stream(ColorType.values()).map(Enum::name).collect(Collectors.toList()).contains(color))){
            return new ResponseEntity<>("Invalid card color", HttpStatus.FORBIDDEN);
        }
        if(cardService.getCurrentClientCardsDTO(authentication).stream().filter(card -> card.getType().name().equals(type)).count() >2){
            return new ResponseEntity<>("You already have 3 Credit Cards", HttpStatus.FORBIDDEN);
        }
        cardService.createCard(currentClient, type, color);
        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication){
        return cardService.getCurrentClientCardsDTO(authentication);
    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam Long cardId){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if(cardId == null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.getCardById(cardId);
        if(card == null){
            return new ResponseEntity<>("Card does not exist", HttpStatus.FORBIDDEN);
        }

        if(!currentClient.getCards().contains(card)){
            return new ResponseEntity<>("Card is not yours", HttpStatus.FORBIDDEN);
        }
        if(!card.isEnabled()){
            return new ResponseEntity<>("Card is already deleted", HttpStatus.FORBIDDEN);
        }

        cardService.deleteCard(cardId);
        return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);
    }
    @PostMapping ("/cards/payment")
    @Transactional
    public ResponseEntity<Object> makePayment(Authentication authentication, @RequestBody CardPaymentDTO cardPaymentDTO){
        Client currentCLient = clientService.getClientByEmail(authentication.getName());
        if(cardPaymentDTO.isSomePropertyNull()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.getCardByNumber(cardPaymentDTO.getCardNumber());
        Account account = accountService.getCurrentClientAccountWithSufficientBalance(authentication, cardPaymentDTO.getPaymentAmount());
        if(card == null){
            return new ResponseEntity<>("Card does not exist", HttpStatus.FORBIDDEN);
        }
        if(card.getThruDate() == LocalDateTime.now()){
            return new ResponseEntity<>("The card is expired", HttpStatus.FORBIDDEN);
        }
        if(!currentCLient.getCards().contains(card)){
            return new ResponseEntity<>("The card does not belong to you", HttpStatus.FORBIDDEN);
        }
        if(card.getCvv() != cardPaymentDTO.getCardCvv()){
            return new ResponseEntity<>("Card cvv is incorrect", HttpStatus.FORBIDDEN);
        }
        if(account==null){
            return new ResponseEntity<>("None of your accounts have sufficient funds to make the payment", HttpStatus.FORBIDDEN);
        }
        cardService.makePayment(account, cardPaymentDTO);
        return new ResponseEntity<>("Payment made successfully", HttpStatus.OK);

    }
}
