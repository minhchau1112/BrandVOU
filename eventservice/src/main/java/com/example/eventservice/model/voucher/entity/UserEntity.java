package com.example.eventservice.model.voucher.entity;

import com.example.eventservice.model.event.entity.AccountEntity;
import com.example.eventservice.model.event.entity.EventEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "accoundID", nullable = false)
    private AccountEntity account;
    @Column(name = "uniqueID")
    private String uniqueID;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "dateOfBirth")
    private String dateOfBirth;
    @Column(name = "gender")
    private String gender;
    @Column(name = "facebookAccount")
    private String facebookAccount;
    @Column(name = "status")
    private String status;
}
