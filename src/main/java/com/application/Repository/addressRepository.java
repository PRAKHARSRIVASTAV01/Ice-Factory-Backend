package com.application.Repository;

import com.application.Object.address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface addressRepository  extends JpaRepository<address, Long> {
}
