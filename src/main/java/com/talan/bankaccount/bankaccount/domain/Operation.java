package com.talan.bankaccount.bankaccount.domain;

import com.talan.bankaccount.bankaccount.util.OperationType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Operation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "main_account")
    @NonNull
    private Account account;
    @ManyToOne
    @JoinColumn(name = "destination_account")
    private Account destinationAccount;
    @NonNull
    private double amount;
    private Date operationDate = new Date();
    @NonNull
    private OperationType type;
}
