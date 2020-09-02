package com.openpayd.rest.dto;

import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.AccountType;
import com.openpayd.repository.entity.BalanceStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {

    private Long id;
    private AccountType accountType;
    private BigDecimal balance;

    private BalanceStatus balanceStatus;

    public static AccountResponse from(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .accountType(account.getType())
                .balanceStatus(account.getStatus())
                .build();
    }
}
