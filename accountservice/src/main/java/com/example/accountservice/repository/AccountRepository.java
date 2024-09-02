package com.example.accountservice.repository;

import com.example.accountservice.model.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    boolean existsAccountEntityByEmail(final String email);
    boolean existsAccountEntityByUsername(final String username);
    Optional<AccountEntity> findAccountEntityByUsername(final String username);

}
