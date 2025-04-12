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
    
    // Fixed query with correct parameter binding
    @Query(value = "SELECT * FROM orders o " +
           "WHERE (:orderDate IS NULL OR DATE(o.oder_date) = DATE(:orderDate)) " + 
           "AND (:deliveryDate IS NULL OR DATE(o.delivery_date) = DATE(:deliveryDate))", 
           nativeQuery = true)
    List<order> findOrdersByOrderDateAndDeliveryDate(
            @Param("orderDate") Date orderDate,
            @Param("deliveryDate") Date deliveryDate);

    // Method to find orders by both dates - fixed
    @Query(value = "SELECT * FROM orders o WHERE DATE(o.oder_date) = DATE(:orderDate) AND DATE(o.delivery_date) = DATE(:deliveryDate)", 
           nativeQuery = true)
    List<order> findOrdersByBothDates(
            @Param("orderDate") Date orderDate,
            @Param("deliveryDate") Date deliveryDate);

    // Method to find orders by order date - fixed
    @Query(value = "SELECT * FROM orders o WHERE DATE(o.oder_date) = DATE(:orderDate)", 
           nativeQuery = true)
    List<order> findOrdersByOrderDateOnly(@Param("orderDate") Date orderDate);

    // Method to find orders by delivery date - fixed
    @Query(value = "SELECT * FROM orders o WHERE DATE(o.delivery_date) = DATE(:deliveryDate)", 
           nativeQuery = true)
    List<order> findOrdersByDeliveryDateOnly(@Param("deliveryDate") Date deliveryDate);
}
