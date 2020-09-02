package com.openpayd.repository.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(length = 512)
    private String primaryAddress;

    @Column(length = 512)
    private String secondaryAddress;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}
