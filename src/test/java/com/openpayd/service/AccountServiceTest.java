package com.openpayd.service;


import com.openpayd.exception.AccountNotFoundException;
import com.openpayd.exception.ClientNotFoundException;
import com.openpayd.repository.AccountRepo;
import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.AccountType;
import com.openpayd.repository.entity.BalanceStatus;
import com.openpayd.repository.entity.Client;
import com.openpayd.rest.dto.CreateAccountRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class AccountServiceTest {

    private AccountRepo accountRepo = mock(AccountRepo.class);
    private ClientService clientService = mock(ClientService.class);

    private AccountService accountService = new AccountService(accountRepo, clientService);

    @Test
    void should_return_account_by_id() {
        Account account = new Account();

        account.setId(1L);
        account.setType(AccountType.CURRENT);
        account.setStatus(BalanceStatus.DR);
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.findAccountById(1L);

        assertEquals(1L, result.getId());
        assertEquals(BalanceStatus.DR, result.getStatus());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
    }

    @Test
    void should_create_account() {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .clientId(999L)
                .balanceStatus(BalanceStatus.CR)
                .balance(BigDecimal.valueOf(100))
                .accountType(AccountType.SAVINGS)
                .build();

        Account expected = new Account();
        expected.setBalance(request.getBalance());
        expected.setStatus(request.getBalanceStatus());
        expected.setType(request.getAccountType());
        expected.setId(1L);

        when(accountRepo.save(any(Account.class))).thenReturn(expected);
        when(clientService.getClientById(999L)).thenReturn(new Client());

        Account result = accountService.createAccount(request);

        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
        assertEquals(AccountType.SAVINGS, result.getType());
        assertEquals(BalanceStatus.CR, result.getStatus());
    }

    @Test
    void should_throw_exception_if_client_not_found() {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .clientId(999L)
                .build();

        when(clientService.getClientById(999L)).thenThrow(new ClientNotFoundException());

        assertThrows(ClientNotFoundException.class, () -> accountService.createAccount(request));
    }

    @Test
    void should_throw_ex_if_account_not_found() {

        when(accountRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(1L));
    }

}
