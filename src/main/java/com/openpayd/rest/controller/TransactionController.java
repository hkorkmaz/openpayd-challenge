package com.openpayd.rest.controller;


import com.openpayd.rest.dto.Result;
import com.openpayd.rest.dto.TransactionResponse;
import com.openpayd.repository.entity.Transaction;
import com.openpayd.rest.dto.TransactionRequest;
import com.openpayd.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<Result> create(@Validated @RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.createTransaction(request);
        return Result.Success()
                .add("transactionId", transaction.getId())
                .build();
    }

    @GetMapping("/transfers/{accountId}")
    public ResponseEntity<Result> listAll(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.listAccountTransactions(accountId);
        List<TransactionResponse> response = transactions.stream()
                .map(TransactionResponse::from)
                .collect(Collectors.toList());

        return Result.Success()
                .add("transactions", response)
                .build();
    }
}
