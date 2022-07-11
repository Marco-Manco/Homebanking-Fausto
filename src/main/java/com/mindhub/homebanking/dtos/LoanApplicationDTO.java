package com.mindhub.homebanking.dtos;

import lombok.Data;

@Data
public class LoanApplicationDTO {
    private Long loanId;
    private Double amount;
    private Integer payments;
    private String destinationAccountNumber;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(Long loanId, Double amount, Integer payments, String destinationAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    //retorna true si alguna propiedad del objeto es null
    public boolean isSomePropertyNull(){
        return (this.loanId ==null || this.amount == null ||
                this.payments == null || this.destinationAccountNumber == null);
    }
}
