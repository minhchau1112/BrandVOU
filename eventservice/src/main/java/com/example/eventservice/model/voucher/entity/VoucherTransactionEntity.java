package com.example.eventservice.model.voucher.entity;

import com.example.eventservice.model.event.entity.AccountEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "VoucherTransaction")
public class VoucherTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "voucherid", nullable = false)
    private VoucherEntity voucher;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "createAt")
    private LocalDateTime createAt;
    @Column(name = "usedQuantity")
    private Integer usedQuantity;
}
