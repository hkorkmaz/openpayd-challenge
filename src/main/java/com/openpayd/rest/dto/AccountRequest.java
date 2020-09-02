package com.openpayd.rest.dto;

import com.openpayd.repository.entity.AccountType;
import com.openpayd.repository.entity.BalanceStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class AccountRequest {

    @NotNull
    private Long clientId;

    @NotNull
    private AccountType accountType;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private BalanceStatus balanceStatus;
}
