package com.example.maildemo.service;

import com.example.maildemo.config.SnsConfig;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;

public class SnsServiceImpl implements CustomPublishService<Map<String, Object>, PublishResponse> {

    private final SnsClient snsClient;
    private final String snsTopicARN;

    public SnsServiceImpl() {
        this.snsClient = SnsConfig.getSnsClient();
        this.snsTopicARN = SnsConfig.getTopicArn();
    }

    @Override
    public PublishResponse doPublish(Map<String, Object> paramMap) {

        PublishRequest request = PublishRequest.builder()
                .topicArn(this.snsTopicARN)
                .subject("안녕하세요")
                .message("내용")
                .build();

        PublishResponse response = snsClient.publish(request);
        snsClient.close();

        return response;
    }
}
