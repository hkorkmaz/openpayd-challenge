package com.openpayd.rest.controller;


import com.openpayd.rest.dto.AccountResponse;
import com.openpayd.rest.dto.Result;
import com.openpayd.rest.dto.CreateAccountRequest;
import com.openpayd.repository.entity.Account;
import com.openpayd.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Result> getById(@PathVariable Long accountId) {
        Account account = accountService.findAccountById(accountId);
        AccountResponse accountResponse = AccountResponse.from(account);

        return Result.Success()
                .add("account", accountResponse)
                .build();
    }

    @PostMapping("/account")
    public ResponseEntity<Result> create(@Validated @RequestBody CreateAccountRequest accountRequest) {
        Account account = accountService.createAccount(accountRequest);
        return Result.Success()
                .add("accountId", account.getId())
                .build();
    }
}
