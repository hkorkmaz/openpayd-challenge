package com.openpayd.service;


import com.openpayd.exception.AccountNotFoundException;
import com.openpayd.repository.AccountRepo;
import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.Client;
import com.openpayd.rest.dto.AccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;
    private final ClientService clientService;

    public Account findAccountById(Long accountId) {
        return accountRepo.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Account createAccount(AccountRequest request) {
        Client client = clientService.getClientById(request.getClientId());

        Account account = new Account();
        account.setType(request.getAccountType());
        account.setStatus(request.getBalanceStatus());
        account.setBalance(request.getBalance());
        account.setClient(client);

        return accountRepo.save(account);
    }

    public void updateBalance(Long accountId, BigDecimal newBalance){
        accountRepo.updateBalance(accountId, newBalance);
    }
}
