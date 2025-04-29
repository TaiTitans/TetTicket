package com.tetticket.ddd.application.service.util.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

   @Async
                    public CompletableFuture<String> uploadProfilePicture(Long userId, MultipartFile file) {
                        String fileName = generateUniqueFileName(file.getOriginalFilename());
                        String folderPath = "profile_picture/" + userId + "/";
                        try {
                            ObjectMetadata metadata = new ObjectMetadata();
                            metadata.setContentType(file.getContentType());
                            metadata.setContentLength(file.getSize());

                            PutObjectRequest request = new PutObjectRequest(
                                    bucketName,
                                    folderPath + fileName,
                                    file.getInputStream(),
                                    metadata
                            );

                            s3Client.putObject(request);
                            String fileUrl = s3Client.getUrl(bucketName, folderPath + fileName).toString();
                            log.info("File uploaded successfully: {}", fileUrl);
                            return CompletableFuture.completedFuture(fileUrl);
                        } catch (IOException e) {
                            log.error("Error uploading file to S3", e);
                            return CompletableFuture.failedFuture(e);
                        }
                    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "-" + originalFilename;
    }
}
