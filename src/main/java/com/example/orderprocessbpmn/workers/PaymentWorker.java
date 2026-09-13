package com.example.orderprocessbpmn.workers;

import com.example.orderprocessbpmn.entity.Product;
import com.example.orderprocessbpmn.entity.User;
import com.example.orderprocessbpmn.repository.ProductRepository;
import com.example.orderprocessbpmn.repository.UserRepository;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class PaymentWorker {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @JobWorker(type = "process-payment")
    public Map<String, Object> processPayment(
            @Variable Long userId,
            @Variable Long productId,
            @Variable Long quantity
            ) {


        Optional<User> potentialUser = userRepository.findById(userId);
        if (potentialUser.isEmpty()){
            return Map.of(
                    "paymentSuccessful", false
            );

        } else {

            Optional<Product> potentialProduct = productRepository.findById(productId);
            if (potentialProduct.isEmpty()){

                return Map.of(
                        "paymentSuccessful", false
                );

            }
            else {
                double amountToPay = quantity * potentialProduct.get().getUnitPrice();

                boolean paymentSuccessfull = amountToPay <= potentialUser.get().getBudget();

                potentialUser.get().setBudget(potentialUser.get().getBudget() - amountToPay);

                userRepository.save(potentialUser.get());

                return Map.of(
                        "paymentSuccessful", paymentSuccessfull
                );
            }

        }

    }
}
