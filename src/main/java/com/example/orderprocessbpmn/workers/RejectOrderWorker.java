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
public class RejectOrderWorker {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @JobWorker(type = "reject-order")
    public Map<String, Object> rejectOrder(@Variable Long userId, @Variable Long productId) {

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

        Order successfullOrder = new Order();
        successfullOrder.setIsSuccessful(false);
        successfullOrder.setUser(potentialUser.get());
        successfullOrder.setPaidAmount(0.0);

        orderRepository.save(successfullOrder);

        return Map.of(
                "deliverySuccessful", false
        );
    }
}