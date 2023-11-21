package com.example.maildemo.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.maildemo.dto.CustomRequest;
import com.example.maildemo.service.SnsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class LambdaFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper;
    private final SnsService publishService;
    private static final Logger log = LoggerFactory.getLogger(LambdaFunction.class);

    public LambdaFunction() {
        this.objectMapper = new ObjectMapper();
        this.publishService = new SnsService();
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {

        try {
            CustomRequest customRequest = objectMapper.readValue(requestEvent.getBody(), CustomRequest.class);

            Object response = publishService.doPublish(customRequest);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(response.toString());
        }
        catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(e.getMessage());
        }
    }
}
