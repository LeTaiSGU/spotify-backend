package com.spotify.spotify_backend.service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AmazonService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(String folder, MultipartFile file, Long id) {
        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = folder + "/" + id + extension;

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            return s3Client.utilities()
                    .getUrl(builder -> builder.bucket(bucketName).key(fileName))
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload file thất bại!", e);
        }
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return (index != -1) ? fileName.substring(index) : "";
    }

    public String getFileUrl(String folder, String name) {
        String fileName = folder + "/" + name;
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucketName).key(fileName))
                .toString();
    }
}
