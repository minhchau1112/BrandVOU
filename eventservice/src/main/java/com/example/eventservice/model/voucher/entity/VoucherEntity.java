package com.example.eventservice.model.voucher.entity;

import com.example.eventservice.model.event.entity.EventEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vouchers")
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "eventID", nullable = false)
    private EventEntity event;
    @Column(name = "code")
    private String code;
    @Column(name = "QRCode")
    private String QRCode;
    @Column(name = "image")
    private String image;
    @Column(name = "value")
    private Float value;
    @Column(name = "description")
    private String description;
    @Column(name = "expirationDate")
    private LocalDateTime expirationDate;
    @Column(name = "status")
    private String status;
    @Column(name = "count")
    private Integer count;
}
