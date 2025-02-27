package com.application.Repository;

import com.application.Object.user_address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface user_addressRepository extends JpaRepository<user_address, String> {
    List<user_address> findByPhone(String phone);

    user_address findByphone(String phone);
}
