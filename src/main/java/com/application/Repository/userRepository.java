package com.application.Repository;

import com.application.Object.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, String> {
}
