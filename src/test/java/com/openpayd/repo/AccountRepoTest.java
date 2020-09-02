package com.openpayd.repo;


import com.openpayd.repository.AccountRepo;
import com.openpayd.repository.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest

class AccountRepoTest {

    @Autowired
    private AccountRepo accountRepo;

    @Test
    @Sql("/sql/account_repo_test.sql")
    void should_update_balance() {
        accountRepo.updateBalance(1L, BigDecimal.valueOf(999));
        Account result = accountRepo.findById(1L).get();
        assertEquals(BigDecimal.valueOf(999).intValue(), result.getBalance().intValue());
    }
}
