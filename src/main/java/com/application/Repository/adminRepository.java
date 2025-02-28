package com.application.Repository;

import com.application.Object.admin;
import com.application.Object.user_credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface adminRepository  extends JpaRepository<admin, Long> {
    admin findByUsername(String username);
}
