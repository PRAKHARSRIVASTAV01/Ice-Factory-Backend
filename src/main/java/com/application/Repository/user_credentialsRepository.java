package com.application.Repository;

import com.application.Object.user_credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface user_credentialsRepository  extends JpaRepository<user_credentials, Long> {
    user_credentials findByPhone(String phone);
}
