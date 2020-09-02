package com.openpayd.rest.dto;

import com.openpayd.repository.entity.Client;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponse {

    private Long id;
    private String name;
    private String surname;
    private String primaryAddress;
    private String secondaryAddress;

    public static ClientResponse from(Client client){
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .primaryAddress(client.getPrimaryAddress())
                .secondaryAddress(client.getSecondaryAddress())
                .build();
    }
}
