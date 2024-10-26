package com.nemo.dong_geul_be.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client() {
        S3Configuration s3Config = S3Configuration.builder()
                .pathStyleAccessEnabled(false)  // Virtual-hosted style
                .build();

        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)  // 서울 리전 설정
                .serviceConfiguration(s3Config)
                .build();
    }

}