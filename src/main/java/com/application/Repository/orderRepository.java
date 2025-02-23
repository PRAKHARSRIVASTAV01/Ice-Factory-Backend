package com.application.Repository;

import com.application.Object.order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface orderRepository extends JpaRepository<order, Long> {
}
