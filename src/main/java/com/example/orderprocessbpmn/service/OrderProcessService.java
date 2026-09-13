package com.example.orderprocessbpmn.service;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderProcessService {

    private final CamundaClient camundaClient;

    public long startOrderProcess(
            Long userId,
            Long productId,
            Long quantity) {

        Map<String, Object> variables = Map.of(
                "userId", userId,
                "productId", productId,
                "quantity", quantity
        );

        ProcessInstanceEvent processInstance =
                camundaClient
                        .newCreateInstanceCommand()
                        .bpmnProcessId("order-process")
                        .latestVersion()
                        .variables(variables)
                        .send()
                        .join();

        return processInstance.getProcessInstanceKey();
    }
}