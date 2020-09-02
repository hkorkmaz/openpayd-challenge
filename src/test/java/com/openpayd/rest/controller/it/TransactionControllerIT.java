package com.openpayd.rest.controller.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openpayd.TestHelper;
import com.openpayd.repository.TransactionRepo;
import com.openpayd.repository.entity.Transaction;
import com.openpayd.rest.dto.TransactionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

import static com.openpayd.TestHelper.jsonResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepo transactionRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Sql("/sql/transaction_it_test.sql")
    void should_create_transaction() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .debitAccount(1000L)
                .creditAccount(1001L)
                .amount(BigDecimal.TEN)
                .message("some-message")
                .build();

        mockMvc.perform(post("/api/transfer")
                .content(TestHelper.asJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.transaction.id").value(1L))
                .andExpect(jsonPath("$.transaction.debitAccount").value(1000))
                .andExpect(jsonPath("$.transaction.creditAccount").value(1001))
                .andExpect(jsonPath("$.transaction.amount").value(10))
                .andExpect(jsonPath("$.transaction.message").value("some-message"));
    }

}
