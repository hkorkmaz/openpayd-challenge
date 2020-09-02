package com.openpayd.rest.controller;

import com.openpayd.repository.entity.Account;
import com.openpayd.repository.entity.Client;
import com.openpayd.rest.dto.ClientRequest;
import com.openpayd.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.openpayd.TestHelper.asJson;
import static com.openpayd.TestHelper.jsonResult;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureRestDocs
class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_client_by_id() throws Exception {
        Client client = createClient(1L);

        when(clientService.getClientById(1L)).thenReturn(client);

        mockMvc.perform(get("/api/client/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.client.id").value(1L))
            .andExpect(jsonPath("$.client.name").value("name1"))
            .andExpect(jsonPath("$.client.surname").value("surname1"))
            .andExpect(jsonPath("$.client.primaryAddress").value("address1"))
            .andDo(document("client/get_client",
                responseFields(
                    fieldWithPath("client.id").description("Client id"),
                    fieldWithPath("client.name").description("Client name"),
                    fieldWithPath("client.surname").description("Client surname"),
                    fieldWithPath("client.primaryAddress").description("Client's primary address"),
                    fieldWithPath("client.secondaryAddress").description("Client's secondary address")
                )
            ));
    }

    @Test
    void should_create_client() throws Exception {
        ClientRequest request = ClientRequest.builder()
                .name("name")
                .surname("surname")
                .primaryAddress("address1")
                .build();

        Client client = createClient(1L);

        when(clientService.createClient(request)).thenReturn(client);

        mockMvc.perform(post("/api/client")
            .content(asJson(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.clientId").value(1L))
            .andDo(document("client/create_client",
                requestFields(
                    fieldWithPath("name").description("Client name, max 255 chars"),
                    fieldWithPath("surname").description("Client surname, max 255 chars"),
                    fieldWithPath("primaryAddress").description("Primary address, max 512 chars"),
                    fieldWithPath("secondaryAddress").description("Secondary address, max 512 chars")
                ),
                responseFields(
                    fieldWithPath("clientId").description("Client id")
                )
            ));
    }

    @Test
    void should_return_all_clients() throws Exception {
        Client client1 = createClient(1L);
        Client client2 = createClient(2L);
        Client client3 = createClient(3L);

        when(clientService.listAll()).thenReturn(Arrays.asList(client1, client2, client3));

        mockMvc.perform(get("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.clients.length()").value(3))
            .andExpect(jsonPath("$.clients[0].id").value(1L))
            .andExpect(jsonPath("$.clients[1].id").value(2L))
            .andExpect(jsonPath("$.clients[2].id").value(3L))
            .andDo(document("client/list_clients",
                responseFields(
                    fieldWithPath("clients[].id").description("Client id"),
                    fieldWithPath("clients[].name").description("Client name"),
                    fieldWithPath("clients[].surname").description("Client surname"),
                    fieldWithPath("clients[].primaryAddress").description("Client primary address"),
                    fieldWithPath("clients[].secondaryAddress").description("Client secondary address, can be null")
                )
            ));
    }

    @Test
    void should_accounts_of_a_client() throws Exception {
        Client client1 = createClient(1L);
        Account a1 = new Account();
        Account a2 = new Account();
        Account a3 = new Account();

        a1.setId(1L);
        a2.setId(2L);
        a3.setId(3L);

        client1.setAccounts(Arrays.asList(a1, a2, a3));

        when(clientService.getClientById(1L)).thenReturn(client1);

        mockMvc.perform(get("/api/client/1/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonResult)
            .andExpect(jsonPath("$.accounts.length()").value(3))
            .andExpect(jsonPath("$.accounts[0].id").value(1L))
            .andExpect(jsonPath("$.accounts[1].id").value(2L))
            .andExpect(jsonPath("$.accounts[2].id").value(3L))
            .andDo(document("client/list_accounts",
                responseFields(
                    fieldWithPath("accounts[].id").description("Account id"),
                    fieldWithPath("accounts[].accountType").description("Account type, can be ['SAVINGS', 'CURRENT']"),
                    fieldWithPath("accounts[].balance").description("Balance of the account"),
                    fieldWithPath("accounts[].balanceStatus").description("Balance status, can be ['DR', 'CR']")
                )
            ));
    }


    private Client createClient(Long id) {
        Client client = new Client();
        client.setId(id);
        client.setName("name" + id);
        client.setSurname("surname" + 1);
        client.setPrimaryAddress("address" + 1);
        return client;
    }
}
