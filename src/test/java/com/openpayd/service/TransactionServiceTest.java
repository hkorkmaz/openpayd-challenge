package com.openpayd.service;

import com.openpayd.exception.AccountNotFoundException;
import com.openpayd.exception.InsufficientBalanceException;
import com.openpayd.exception.InvalidBalanceStatusException;
import com.openpayd.repository.TransactionRepo;
import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.BalanceStatus;
import com.openpayd.repository.entity.Transaction;
import com.openpayd.rest.dto.TransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransactionServiceTest {


    private TransactionRepo transactionRepo = mock(TransactionRepo.class);
    private AccountService accountService = mock(AccountService.class);

    private TransactionService transactionService = new TransactionService(transactionRepo, accountService);

    private Account debitAccount = new Account();
    private Account creditAccount = new Account();

    @BeforeEach
    void init() {
        debitAccount.setId(111L);
        debitAccount.setStatus(BalanceStatus.DR);

        creditAccount.setId(222L);
        creditAccount.setStatus(BalanceStatus.CR);
    }

    @Test
    void should_create_transaction() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .debitAccount(111L)
                .creditAccount(222L)
                .message("Some message")
                .build();

        debitAccount.setBalance(BigDecimal.valueOf(200));
        creditAccount.setBalance(BigDecimal.valueOf(500));

        Transaction expected = new Transaction();
        expected.setAmount(BigDecimal.valueOf(100));
        expected.setMessage("Some message");
        expected.setCreditAccount(creditAccount);
        expected.setDebitAccount(debitAccount);

        when(accountService.findAccountById(111L)).thenReturn(debitAccount);
        when(accountService.findAccountById(222L)).thenReturn(creditAccount);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(expected);

        Transaction result = transactionService.createTransaction(request);

        verify(accountService).updateBalance(111L, BigDecimal.valueOf(300));
        verify(accountService).updateBalance(222L, BigDecimal.valueOf(400));

        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        assertEquals("Some message", result.getMessage());

    }

    @Test
    void should_throw_ex_if_account_not_found() {
        TransactionRequest request = TransactionRequest.builder()
                .debitAccount(111L)
                .build();

        when(accountService.findAccountById(111L)).thenThrow(new AccountNotFoundException());

        assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(request));
    }

    @Test
    void should_throw_ex_if_balance_not_enough() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(1000))
                .debitAccount(111L)
                .creditAccount(222L)
                .message("Some message")
                .build();

        creditAccount.setBalance(BigDecimal.valueOf(0));

        when(accountService.findAccountById(111L)).thenReturn(debitAccount);
        when(accountService.findAccountById(222L)).thenReturn(creditAccount);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.createTransaction(request));
    }

    @Test
    void should_throw_ex_if_account_status_invalid_credit() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .debitAccount(111L)
                .creditAccount(222L)
                .build();

        creditAccount.setStatus(BalanceStatus.DR);

        when(accountService.findAccountById(111L)).thenReturn(debitAccount);
        when(accountService.findAccountById(222L)).thenReturn(creditAccount);

        assertThrows(InvalidBalanceStatusException.class, () -> transactionService.createTransaction(request));
    }

    @Test
    void should_throw_ex_if_account_status_invalid_debit() {
        TransactionRequest request = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .debitAccount(111L)
                .creditAccount(222L)
                .build();

        debitAccount.setStatus(BalanceStatus.CR);

        when(accountService.findAccountById(111L)).thenReturn(debitAccount);
        when(accountService.findAccountById(222L)).thenReturn(creditAccount);

        assertThrows(InvalidBalanceStatusException.class, () -> transactionService.createTransaction(request));
    }

    @Test
    void should_list_transactions_for_account() {
        Account account = new Account();
        account.setId(999L);

        Transaction t1 = new Transaction();
        t1.setId(1L);

        Transaction t2 = new Transaction();
        t2.setId(2L);

        when(accountService.findAccountById(1L)).thenReturn(account);
        when(transactionRepo.findByAccountId(999L)).thenReturn(Arrays.asList(t1, t2));

        List<Transaction> result = transactionService.listAccountTransactions(1L);

        assertEquals(2, result.size());
    }

}
