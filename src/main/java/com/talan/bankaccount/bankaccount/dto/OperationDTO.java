package com.talan.bankaccount.bankaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OperationDTO {
    private Long accountNumber;
    private Double amount;

}
