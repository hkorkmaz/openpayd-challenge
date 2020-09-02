package com.openpayd.repo;

import com.openpayd.repository.TransactionRepo;
import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TransactionRepoTest {

    @Autowired
    private TransactionRepo transactionRepo;

    @Test
    @Sql("/sql/transaction_repo_test.sql")
    void should_return_transations_by_account() {

        Account account = new Account();
        account.setId(1000L);

        Account account1 = new Account();
        account1.setId(1001L);

        Account account2 = new Account();
        account2.setId(1002L);

        Account account3 = new Account();
        account3.setId(1003L);

        Transaction t1 = new Transaction();
        t1.setDebitAccount(account);
        t1.setCreditAccount(account1);
        t1.setAmount(BigDecimal.valueOf(10));

        Transaction t2 = new Transaction();
        t2.setDebitAccount(account);
        t2.setCreditAccount(account2);
        t2.setAmount(BigDecimal.valueOf(20));

        Transaction t3 = new Transaction();
        t3.setDebitAccount(account3);
        t3.setCreditAccount(account);
        t3.setAmount(BigDecimal.valueOf(30));

        Transaction t4 = new Transaction();
        t4.setDebitAccount(account2);
        t4.setCreditAccount(account3);
        t4.setAmount(BigDecimal.valueOf(40));

        transactionRepo.save(t1);
        transactionRepo.save(t2);
        transactionRepo.save(t3);
        transactionRepo.save(t4);


        List<Transaction> result = transactionRepo.findByAccountId(1000L);

        assertEquals(result.size(), 3);
    }

}
