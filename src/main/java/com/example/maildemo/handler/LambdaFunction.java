package com.example.maildemo.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.maildemo.dto.CustomRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.function.Function;

public class LambdaFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(LambdaFunction.class);

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {

        try {
            String body = requestEvent.getBody();
            CustomRequest customRequest = objectMapper.readValue(body, CustomRequest.class);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.OK.value())
                    .withBody(customRequest.toString());
        }
        catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withBody(e.getMessage());
        }
    }
}
