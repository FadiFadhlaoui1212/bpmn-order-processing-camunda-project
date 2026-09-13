package com.example.orderprocessbpmn.workers;

import com.example.orderprocessbpmn.entity.Order;
import com.example.orderprocessbpmn.entity.Product;
import com.example.orderprocessbpmn.entity.User;
import com.example.orderprocessbpmn.repository.OrderRepository;
import com.example.orderprocessbpmn.repository.ProductRepository;
import com.example.orderprocessbpmn.repository.UserRepository;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class DeliveryWorker {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @JobWorker(type = "deliver-order")
    public Map<String, Object> deliverOrder(
            @Variable Long userId,
            @Variable Long productId,
            @Variable Long quantity
    ) {

        Optional<User> potentialUser = userRepository.findById(userId);
        if (potentialUser.isEmpty()){

            return Map.of(
                    "deliverySuccessful", false
            );

        }

        Optional<Product> potentialProduct = productRepository.findById(productId);


        if (potentialProduct.isEmpty()){

            return Map.of(
                    "deliverySuccessful", false
            );

        }

        double amountToPay = quantity * potentialProduct.get().getUnitPrice();

        potentialUser.get().setBudget(potentialUser.get().getBudget() - amountToPay);

        userRepository.save(potentialUser.get());


        potentialProduct.get().setStockQuantity(potentialProduct.get().getStockQuantity() - quantity);
        productRepository.save(potentialProduct.get());

        Order successfullOrder = new Order();
        successfullOrder.setIsSuccessful(true);
        successfullOrder.setUser(potentialUser.get());
        successfullOrder.setPaidAmount(potentialProduct.get().getStockQuantity() * potentialProduct.get().getUnitPrice());

        orderRepository.save(successfullOrder);

        return Map.of(
                "deliverySuccessful", true
        );
    }
}