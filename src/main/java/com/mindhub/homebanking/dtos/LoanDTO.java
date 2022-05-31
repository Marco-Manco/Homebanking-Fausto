package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
@Data
public class LoanDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private List<ClientDTO> clients = new ArrayList<>();

    public LoanDTO(){}
    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.clients = loan.getClients().stream().map(ClientDTO::new).collect(toList());
    }
}
