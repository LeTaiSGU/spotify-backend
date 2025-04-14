package com.spotify.spotify_backend.service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AmazonService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(String folder, MultipartFile file, Long id) {
        long startTime = System.currentTimeMillis(); // Bắt đầu tính thời gian

        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = folder + "/" + id + extension;

        // Buffer lớn hơn (128KB thay vì mặc định 8KB)
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream(), 64 * 1024)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            long endTime = System.currentTimeMillis(); // Kết thúc tính thời gian
            System.out.println("Upload hoàn tất trong " + (endTime - startTime) + "ms");

            return s3Client.utilities()
                    .getUrl(builder -> builder.bucket(bucketName).key(fileName))
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload file thất bại!", e);
        }
    }

    public String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return (index != -1) ? fileName.substring(index) : "";
    }

    public String getFileUrl(String folder, String name) {
        String fileName = folder + "/" + name;
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucketName).key(fileName))
                .toString();
    }

    // Xoa file trong s3
    public void deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new RuntimeException("Xoá file thất bại: " + key, e);
        }
    }

}
