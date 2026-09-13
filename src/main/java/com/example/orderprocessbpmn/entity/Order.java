package com.example.orderprocessbpmn.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double paidAmount;

    private Boolean isSuccessful;


}
