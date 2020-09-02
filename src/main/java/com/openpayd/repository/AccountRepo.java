package com.openpayd.repository;

import com.openpayd.repository.entity.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface AccountRepo extends CrudRepository<Account, Long> {

    @Modifying
    @Query("UPDATE Account a SET a.balance = :newBalance WHERE a.id = :accountId")
    void updateBalance(@Param("accountId") Long accountId, @Param("newBalance") BigDecimal newBalance);
}
