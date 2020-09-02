package com.openpayd.service;


import com.openpayd.exception.InsufficientBalanceException;
import com.openpayd.exception.InvalidBalanceStatusException;
import com.openpayd.repository.TransactionRepo;
import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.BalanceStatus;
import com.openpayd.repository.entity.Transaction;
import com.openpayd.rest.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    final TransactionRepo transactionRepo;
    final AccountService accountService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public Transaction createTransaction(TransactionRequest request) {
        Account debitAccount = accountService.findAccountById(request.getDebitAccount());
        Account creditAccount = accountService.findAccountById(request.getCreditAccount());

        if(debitAccount.getStatus() != BalanceStatus.DR)
            throw new InvalidBalanceStatusException("Debit account's balance status must be 'DR'");

        if(creditAccount.getStatus() != BalanceStatus.CR)
            throw new InvalidBalanceStatusException("Credit account's balance status must be 'CR'");

        if(creditAccount.getBalance().compareTo(request.getAmount()) < 0)
            throw new InsufficientBalanceException();

        Transaction transaction = new Transaction();
        transaction.setDebitAccount(debitAccount);
        transaction.setCreditAccount(creditAccount);
        transaction.setMessage(request.getMessage());
        transaction.setAmount(request.getAmount());

        Transaction created = transactionRepo.save(transaction);

        BigDecimal newDebitBalance = debitAccount.getBalance().plus().add(request.getAmount());
        BigDecimal newCreditBalance = creditAccount.getBalance().subtract(request.getAmount());

        accountService.updateBalance(debitAccount.getId(), newDebitBalance);
        accountService.updateBalance(creditAccount.getId(), newCreditBalance);

        return created;
    }

    public List<Transaction> listAccountTransactions(Long accountId) {
        Account account = accountService.findAccountById(accountId);
        return new ArrayList<>(transactionRepo.findByAccountId(account.getId()));
    }
}
