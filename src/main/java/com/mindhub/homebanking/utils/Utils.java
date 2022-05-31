package com.mindhub.homebanking.utils;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;

import java.util.List;


public class Utils {

    public static String getRandomAccountNumber(AccountRepository accountRepository){
        boolean isRepeated = true;
        String newAccountNumber = "";
        while(isRepeated){
            String accountNumberAux = "VIN" + ((int) (10000000 + Math.random()*90000000));
            if(!accountRepository.existsByNumber(accountNumberAux)){
                newAccountNumber = accountNumberAux;
                isRepeated = false;
            }
        }
        return newAccountNumber;
    }
    //este se parece al de arriba, buscar forma de que se vaya 1!
    public static String getRandomCardNumber(CardRepository cardRepository){
        boolean isRepeated = true;
        String newCardNumber = "";
        while(isRepeated){
            String cardNumber = "";
            for (int i = 0; i < 4; i++) {
                cardNumber += (int) (1000 + Math.random()*9000);
            }
            if(!cardRepository.existsByNumber(cardNumber)){
                newCardNumber = cardNumber;
                isRepeated = false;
            }
        }
        return newCardNumber;
    }
}
