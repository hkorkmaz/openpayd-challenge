package com.openpayd.rest.controller;

import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.AccountType;
import com.openpayd.repository.entity.BalanceStatus;
import com.openpayd.rest.dto.AccountRequest;
import com.openpayd.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.openpayd.TestHelper.asJson;
import static com.openpayd.TestHelper.jsonResult;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureRestDocs
class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_account_by_id() throws Exception {
        Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        account.setStatus(BalanceStatus.DR);
        account.setType(AccountType.CURRENT);
        account.setId(1L);

        when(accountService.findAccountById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.account.id").value(1L))
            .andExpect(jsonPath("$.account.balance").value(10))
            .andExpect(jsonPath("$.account.balanceStatus").value("DR"))
            .andExpect(jsonPath("$.account.accountType").value("CURRENT"))
            .andDo(document("account/list_account",
                responseFields(
                    fieldWithPath("account.id").description(""),
                    fieldWithPath("account.accountType").description("Account type, can be ['SAVINGS', 'CURRENT']"),
                    fieldWithPath("account.balance").description("Initial balance of the account"),
                    fieldWithPath("account.balanceStatus").description("Balance status, can be ['DR', 'CR']")
                )
            ));
    }

    @Test
    void should_create_account() throws Exception {
        AccountRequest request = AccountRequest.builder()
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.TEN)
                .balanceStatus(BalanceStatus.CR)
                .clientId(999L)
                .build();

        Account account = new Account();
        account.setBalance(request.getBalance());
        account.setStatus(request.getBalanceStatus());
        account.setType(request.getAccountType());
        account.setId(1L);

        when(accountService.createAccount(request)).thenReturn(account);

        mockMvc.perform(post("/api/account")
            .content(asJson(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.accountId").value(1L))
            .andDo(document("account/create_account",
                requestFields(
                    fieldWithPath("clientId").description(""),
                    fieldWithPath("accountType").description("Account type, can be ['SAVINGS', 'CURRENT']"),
                    fieldWithPath("balance").description("Initial balance of the account"),
                    fieldWithPath("balanceStatus").description("Balance status, can be ['DR', 'CR']")
                ),
                responseFields(
                    fieldWithPath("accountId").type(NUMBER).description("Id for newly created account")
                )
            ));
    }
}
