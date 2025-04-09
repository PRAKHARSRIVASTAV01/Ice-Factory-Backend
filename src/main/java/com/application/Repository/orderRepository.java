package com.application.Repository;

import com.application.Object.order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface orderRepository extends JpaRepository<order, Long> {

    @Query("SELECT o FROM order o WHERE o.phone = :phone ORDER BY o.oderDate DESC")
    List<order> findOrdersByPhoneOrderByDateDesc(@Param("phone") String phone);

    @Query("SELECT o FROM order o JOIN order_status s ON o.id = s.id " +
           "WHERE o.deliveryDate BETWEEN :startDate AND :endDate " +
           "AND s.status IN ('placed', 'processing')")
    List<order> findFutureOrdersForForecast(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
