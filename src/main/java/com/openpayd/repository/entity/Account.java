package com.openpayd.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, scale = 2)
    private BigDecimal balance = BigDecimal.valueOf(0);

    @Column(nullable = false)
    private BalanceStatus status;

    @Column(nullable = false)
    private AccountType type;

    @ManyToOne
    private Client client;

    private LocalDateTime createdAt = LocalDateTime.now();
}
