package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getRandomCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam String color, @RequestParam String type) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());
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
        if(currentClient.getCards().stream().filter(card -> card.getType().name().equals(type)).count() >2){
            return new ResponseEntity<>("You already have 3 Credit Cards", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(currentClient.getFullName(), CardType.valueOf(type), ColorType.valueOf(color),
                getRandomCardNumber(cardRepository), (int) (100 + Math.random()*900), LocalDateTime.now(),
                LocalDateTime.now().plusYears(5));

        currentClient.addCard(newCard);
        clientRepository.save(currentClient);
        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication){
        return (new ClientDTO(clientRepository.findByEmail(authentication.getName()))).getCards();

    }
}
