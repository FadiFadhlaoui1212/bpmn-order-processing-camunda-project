package com.example.orderprocessbpmn.workers;

import com.example.orderprocessbpmn.entity.Product;
import com.example.orderprocessbpmn.repository.ProductRepository;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class CheckStockWorker {


    @Autowired
    private ProductRepository productRepository;

    @JobWorker(type = "check-stock")
    public Map<String, Object> checkStock(
            @Variable Long productId,
            @Variable Long quantity) {

        Optional<Product> potentialProduct = productRepository.findById(productId);
        if (potentialProduct.isEmpty()){

            return Map.of(
                    "isStockAvailable", false
            );

        } else {

            Product product = potentialProduct.get();
            if (product.getStockQuantity() < quantity){

                return Map.of(
                        "isStockAvailable", false
                );

            }
            else {

                return Map.of(
                        "isStockAvailable", true
                );

            }

        }
    }
}
