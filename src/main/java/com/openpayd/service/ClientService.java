package com.openpayd.service;


import com.openpayd.exception.ClientNotFoundException;
import com.openpayd.repository.ClientRepo;
import com.openpayd.repository.entity.Client;
import com.openpayd.rest.dto.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepo clientRepo;

    public Client getClientById(Long clientId) {
        return clientRepo.findById(clientId).orElseThrow(ClientNotFoundException::new);
    }

    public Client createClient(ClientRequest request) {
        Client client = new Client();

        client.setName(request.getName());
        client.setSurname(request.getSurname());
        client.setPrimaryAddress(request.getPrimaryAddress());
        client.setSecondaryAddress(request.getSecondaryAddress());

        return clientRepo.save(client);
    }

    public List<Client> listAll() {
        List<Client> result = new ArrayList<>();
        clientRepo.findAll().forEach(result::add);
        return result;
    }
}
