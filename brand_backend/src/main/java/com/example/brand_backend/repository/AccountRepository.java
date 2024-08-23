
package com.example.brand_backend.repository;

import com.example.brand_backend.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Accounts, Long> {
    Accounts findByUsername(String username);
}
