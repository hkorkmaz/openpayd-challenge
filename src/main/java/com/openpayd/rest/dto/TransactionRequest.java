package com.openpayd.rest.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequest {

    @NotNull
    private Long debitAccount;

    @NotNull
    private Long creditAccount;

    @NotNull
    private BigDecimal amount;

    private String message = "";
}
