package com.application.Repository;

import com.application.Object.notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface notificationRepository extends JpaRepository<notification, Long> {
    List<notification> findByRecipient(String recipient);
    List<notification> findByStatus(String status);
    List<notification> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<notification> findByNotificationType(String notificationType);
}