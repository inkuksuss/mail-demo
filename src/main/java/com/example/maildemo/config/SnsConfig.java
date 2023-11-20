package com.example.maildemo.config;

import org.yaml.snakeyaml.Yaml;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SnsConfig {

    private static SnsClient snsClient;

    private static String awsAccessKey;

    private static String awsSecretKey;

    private static String awsRegion;

    private static String topicArn;

    private SnsConfig() {}

    private static void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = SnsConfig.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (inputStream == null) throw new RuntimeException("Can not init SnsConfig");
            Map<String, Object> data = yaml.load(inputStream);
            Map<String, Object> awsConfig = (Map<String, Object>) data.get("aws");
            awsAccessKey = (String) awsConfig.get("access-key");
            awsSecretKey = (String) awsConfig.get("secret-key");
            awsRegion = (String) awsConfig.get("region");

            Map<String, Object> snsConfig = (Map<String, Object>) awsConfig.get("sns");
            topicArn = (String) snsConfig.get("topic-arn");
        }
        catch (IOException e) {
            throw new RuntimeException("Can not init SnsConfig");
        }
    }

    public static SnsClient getSnsClient() {
        if (snsClient == null) {
            loadConfig();
            return SnsClient.builder()
                    .credentialsProvider(
                            getAwsCredentials(awsAccessKey, awsSecretKey)
                    )
                    .region(Region.of(awsRegion))
                    .build();
        }
        else return snsClient;
    }

    private static AwsCredentialsProvider getAwsCredentials(String accessKey, String secretKey) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return () -> awsBasicCredentials;
    }

    public static String getTopicArn() {
        if (topicArn == null) loadConfig();
        return topicArn;
    }
}
