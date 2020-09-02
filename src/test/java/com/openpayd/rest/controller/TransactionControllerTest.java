package com.openpayd.rest.controller;

import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.Transaction;
import com.openpayd.rest.dto.TransactionRequest;
import com.openpayd.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.openpayd.TestHelper.asJson;
import static com.openpayd.TestHelper.jsonResult;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureRestDocs
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_create_transaction() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .debitAccount(1L)
                .creditAccount(2L)
                .amount(BigDecimal.TEN)
                .message("some-message")
                .build();

        Transaction created = createTransaction(1L, 2L, 1L, BigDecimal.TEN);

        when(transactionService.createTransaction(request)).thenReturn(created);

        mockMvc.perform(post("/api/transfer")
            .content(asJson(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.transactionId").value(1L))
            .andDo(document("transaction/create",
                requestFields(
                    fieldWithPath("amount").description("Transaction amount"),
                    fieldWithPath("debitAccount").description("Debit account id"),
                    fieldWithPath("creditAccount").description("Credit account id"),
                    fieldWithPath("message").description("Message, optional")
                ),
                responseFields(
                    fieldWithPath("transactionId").description("Created transaction id")
                )
            ));
    }

    @Test
    void should_list_transactions_for_given_Account() throws Exception {

        Transaction t1 = createTransaction(1L, 2L, 1L, BigDecimal.TEN);
        Transaction t2 = createTransaction(2L, 1L, 2L, BigDecimal.TEN);
        Transaction t3 = createTransaction(1L, 3L, 3L, BigDecimal.TEN);

        when(transactionService.listAccountTransactions(1L)).thenReturn(Arrays.asList(t1, t2, t3));

        mockMvc.perform(get("/api/transfers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.transactions.length()").value(3))
            .andExpect(jsonPath("$.transactions[0].id").value(1))
            .andExpect(jsonPath("$.transactions[1].id").value(2))
            .andExpect(jsonPath("$.transactions[2].id").value(3))
            .andDo(document("transaction/list",
                responseFields(
                    fieldWithPath("transactions.[].id").description("Transaction id"),
                    fieldWithPath("transactions.[].amount").description("Transaction amount"),
                    fieldWithPath("transactions.[].debitAccount").description("Debit account id"),
                    fieldWithPath("transactions.[].creditAccount").description("Credit account id"),
                    fieldWithPath("transactions.[].message").description("Message, optional"),
                    fieldWithPath("transactions.[].createdAt").description("Transaction date")
                )
            ));
    }


    private Transaction createTransaction(Long debitAccountId, Long creditAccountId, Long id, BigDecimal amount) {
        Account debitAccount = new Account();
        debitAccount.setId(debitAccountId);

        Account creditAccount = new Account();
        creditAccount.setId(creditAccountId);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDebitAccount(debitAccount);
        transaction.setCreditAccount(creditAccount);
        transaction.setMessage("some-message");
        transaction.setId(id);

        return transaction;
    }
}
