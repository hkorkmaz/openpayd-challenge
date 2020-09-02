package com.openpayd.rest.controller;


import com.openpayd.rest.dto.AccountResponse;
import com.openpayd.rest.dto.ClientResponse;
import com.openpayd.rest.dto.ClientRequest;
import com.openpayd.rest.dto.Result;
import com.openpayd.repository.entity.Client;
import com.openpayd.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Result> getById(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        return Result.Success()
                .add("client", ClientResponse.from(client))
                .build();
    }

    @PostMapping("/client")
    public ResponseEntity<Result> create(@Validated @RequestBody ClientRequest clientRequest) {
        Client client = clientService.createClient(clientRequest);
        return Result.Success()
                .add("clientId", client.getId())
                .build();
    }

    @GetMapping("/clients")
    public ResponseEntity<Result> listClients() {
        List<Client> clients = clientService.listAll();
        List<ClientResponse> response = clients.stream()
                .map(ClientResponse::from)
                .collect(Collectors.toList());

        return Result.Success()
                .add("clients", response)
                .build();
    }

    @GetMapping("/client/{clientId}/accounts")
    public ResponseEntity<Result> listAccounts(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        List<AccountResponse> accounts = client.getAccounts().stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());

        return Result.Success()
                .add("accounts", accounts)
                .build();
    }
}
