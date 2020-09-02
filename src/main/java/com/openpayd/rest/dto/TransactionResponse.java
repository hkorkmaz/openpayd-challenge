package com.openpayd.rest.dto;

import com.openpayd.repository.entity.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private Long id;
    private Long debitAccount;
    private Long creditAccount;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String message;


    public static TransactionResponse from(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .debitAccount(transaction.getDebitAccount().getId())
                .creditAccount(transaction.getCreditAccount().getId())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .message(transaction.getMessage())
                .build();
    }
}
