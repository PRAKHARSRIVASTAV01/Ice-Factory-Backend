package com.application.Object;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String recipient;
    private String message;
    private String status; // PENDING, SENT, FAILED
    private LocalDateTime createdAt;
    private String notificationType; // ORDER_PLACED, ORDER_SHIPPED, ORDER_DELIVERED, etc.
    
    @Column(length = 1000)
    private String responseDetails;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getNotificationType() {
        return notificationType;
    }
    
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    
    public String getResponseDetails() {
        return responseDetails;
    }
    
    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }
}