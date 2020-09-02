package com.openpayd.service;

import com.openpayd.exception.ClientNotFoundException;
import com.openpayd.repository.ClientRepo;
import com.openpayd.repository.entity.Client;
import com.openpayd.rest.dto.CreateClientRequest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientServiceTest {


    private ClientRepo clientRepo = mock(ClientRepo.class);

    private ClientService clientService = new ClientService(clientRepo);


    @Test
    void should_return_by_client_id() {
        Client expected = new Client();
        expected.setId(1L);
        expected.setName("name");
        expected.setSurname("surname");

        when(clientRepo.findById(1L)).thenReturn(Optional.of(expected));

        Client result = clientService.getClientById(1L);

        assertEquals(1L, result.getId());
        assertEquals("name", result.getName());
        assertEquals("surname", result.getSurname());
    }

    @Test
    void should_create_client(){

        CreateClientRequest request = CreateClientRequest.builder()
                .name("name")
                .surname("surname")
                .primaryAddress("address1")
                .build();

        Client expected = new Client();
        expected.setId(1L);
        expected.setName(request.getName());
        expected.setSurname(request.getSurname());
        expected.setPrimaryAddress(request.getPrimaryAddress());

        when(clientRepo.save(any(Client.class))).thenReturn(expected);

        Client client = clientService.createClient(request);

        assertEquals("name", client.getName());
        assertEquals("surname", client.getSurname());
        assertEquals("address1", client.getPrimaryAddress());
        assertEquals(1L, client.getId());
    }

    @Test
    void should_throw_exception_if_client_not_found() {

        when(clientRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
    }
}
