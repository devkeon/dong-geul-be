package com.nemo.dong_geul_be.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final String BUCKET_NAME = "donggeul";

    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(fileName)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, file.getSize())
            );
            // 파일 업로드 후 URL 반환
            return getFileUrl(fileName);
        } catch (S3Exception | IOException e) {
            System.err.println("파일 업로드 오류: " + e.getMessage());
            throw new RuntimeException("S3에 파일을 업로드하는 중 오류가 발생했습니다.", e);
        }
    }

    private String getFileUrl(String fileName) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(BUCKET_NAME).key(fileName)).toExternalForm();
    }
}
