package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
//cambiar este nombre horrible!
public class LoanVisibleToTheCLientDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private Float interestPercentage;

    public LoanVisibleToTheCLientDTO(){}

    public LoanVisibleToTheCLientDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interestPercentage = loan.getInterestPercentage();
    }
}
