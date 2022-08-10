package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoanDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private Float interestPercentage;

    public LoanDTO(){}

    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interestPercentage = loan.getInterestPercentage();
    }
}
