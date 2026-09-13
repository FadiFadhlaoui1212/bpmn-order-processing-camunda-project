package com.example.orderprocessbpmn.controller;

import com.example.orderprocessbpmn.service.OrderProcessService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderProcessController {

    private final OrderProcessService orderProcessService;

    public OrderProcessController(OrderProcessService orderProcessService) {
        this.orderProcessService = orderProcessService;
    }

    @PostMapping
    public String createOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Long quantity
    ) {

        orderProcessService.startOrderProcess(
                userId,
                productId,
                quantity
        );

        return "Order process started";
    }
}