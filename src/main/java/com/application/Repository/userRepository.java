package com.application.Repository;

import com.application.Object.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface userRepository extends JpaRepository<user, String> {
    user findByPhone(String phone);

    List<user> findByFirstName(String firstName);

    List<user> findByLastName(String lastName);

    List<user> findByAddress(String address);
}
