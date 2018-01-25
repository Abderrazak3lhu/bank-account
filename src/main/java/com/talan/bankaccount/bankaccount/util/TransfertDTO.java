package com.talan.bankaccount.bankaccount.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransfertDTO {
    private Long mainAccountNumber;
    private Long destinationAccountNumber;
    private Double amount;
}
