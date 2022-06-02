package com.mindhub.homebanking.dtos;

import lombok.Data;

@Data
public class LoanApplicationDTO {
    private Long loanId;
    private Double amount;
    private Integer payments;
    private String destinationAccountNumber;

    public LoanApplicationDTO(){}

    //retorna true si alguna propiedad del objeto es null
    public boolean isSomePropertyNull(){
        return (this.loanId ==null || this.amount == null ||
                this.payments == null || this.destinationAccountNumber == null);
    }
}
