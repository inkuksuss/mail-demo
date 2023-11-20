package com.example.maildemo.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.maildemo.dto.CustomRequest;
import com.example.maildemo.service.CustomPublishService;
import com.example.maildemo.service.SnsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LambdaFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper;
    private final CustomPublishService<Map<String, Object>, ?> publishService;
    private static final Logger log = LoggerFactory.getLogger(LambdaFunction.class);

    public LambdaFunction() throws FileNotFoundException {
        this.objectMapper = new ObjectMapper();
        this.publishService = new SnsServiceImpl();
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {

        try {
            String body = requestEvent.getBody();
            CustomRequest customRequest = objectMapper.readValue(body, CustomRequest.class);

            Object response = publishService.doPublish(new HashMap<>());

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.OK.value())
                    .withBody(response.toString());
        }
        catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withBody(e.getMessage());
        }
    }
}
