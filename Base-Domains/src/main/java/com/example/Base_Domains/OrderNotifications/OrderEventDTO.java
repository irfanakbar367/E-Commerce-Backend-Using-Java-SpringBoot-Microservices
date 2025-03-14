package com.example.Base_Domains.OrderNotifications;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderEventDTO {

    private int id;
    private int userId;
    private List<Integer> productIds;

    public OrderEventDTO(int id, int userId, List<Integer> productIds, BigDecimal totalAmount, String status, LocalDateTime orderDate, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.updatedAt = updatedAt;
    }

    public OrderEventDTO() {
    }

    private BigDecimal totalAmount;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;


}
