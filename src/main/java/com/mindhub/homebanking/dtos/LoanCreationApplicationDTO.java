package com.mindhub.homebanking.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoanCreationApplicationDTO {
    private String name;
    private Double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private Float interestPercentage;

    public LoanCreationApplicationDTO(){}

    public boolean isSomePropertyNull(){
        return (this.name ==null || this.maxAmount == null ||
                this.payments.size()==0 || this.interestPercentage == null);
    }
    public boolean isSomePaymentNegative(){
        return this.payments.stream().anyMatch(payment -> payment < 0);
    }
}
