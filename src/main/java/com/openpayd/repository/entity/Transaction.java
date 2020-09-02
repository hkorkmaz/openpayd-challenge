package com.openpayd.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Account debitAccount;

    @ManyToOne(optional = false)
    private Account creditAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();
}
