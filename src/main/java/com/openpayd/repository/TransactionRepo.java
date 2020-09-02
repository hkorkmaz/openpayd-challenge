package com.openpayd.repository;

import com.openpayd.repository.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepo extends CrudRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.debitAccount.id = :accountId OR t.creditAccount.id = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);
}
