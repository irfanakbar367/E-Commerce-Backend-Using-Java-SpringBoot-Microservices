package com.example.Base_Domains.OrderNotifications;

public class OrderEvent {

    private String status;

    public OrderEvent() {
    }

    private String message;

    public OrderEvent(String status, String message, OrderEventDTO orderEventDTO) {
        this.status = status;
        this.message = message;
        this.orderEventDTO = orderEventDTO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderEventDTO getOrderEventDTO() {
        return orderEventDTO;
    }

    public void setOrderEventDTO(OrderEventDTO orderEventDTO) {
        this.orderEventDTO = orderEventDTO;
    }

    private OrderEventDTO orderEventDTO;

}
