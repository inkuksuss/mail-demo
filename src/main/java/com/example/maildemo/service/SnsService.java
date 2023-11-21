package com.example.maildemo.service;

import com.example.maildemo.config.SnsConfig;
import com.example.maildemo.dto.CustomRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

public class SnsService {

    private final SnsClient snsClient;
    private final String snsTopicARN;

    public SnsService() {
        this.snsClient = SnsConfig.getSnsClient();
        this.snsTopicARN = SnsConfig.getTopicArn();
    }

    public PublishResponse doPublish(CustomRequest request) {
        PublishRequest message = PublishRequest.builder()
//                .topicArn(this.snsTopicARN)
                .subject(request.getSubject())
                .phoneNumber(request.getPhoneNumber())
                .message(request.getSubject())
                .build();

        PublishResponse response = snsClient.publish(message);
        snsClient.close();

        return response;
    }
}
