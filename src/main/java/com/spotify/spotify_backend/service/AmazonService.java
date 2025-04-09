package com.spotify.spotify_backend.service;

import com.spotify.spotify_backend.model.Artist;
import com.spotify.spotify_backend.model.Song;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonService {

    private final S3Client s3Client;  // Thay AmazonS3Client thành S3Client của SDK 2.x

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(String folder, MultipartFile file, long id) {
        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = folder + "/" + id + "_" + UUID.randomUUID() + extension;

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

    public boolean uploadArtistImage(MultipartFile file, String artistName) {

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));

        // Tạo tên file mới với artistName và đuôi file
        String fileName = "artist_img/" +  artistName + fileExtension;
        System.out.println("File name: " + fileName);
        try (InputStream inputStream = file.getInputStream()) {
            // Tạo PutObjectRequest với các tham số thích hợp
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            // Upload file lên S3
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            // Kiểm tra URL đã được tạo và trả về true nếu thành công
            String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toString();
            System.out.println("File URL: " + fileUrl);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Upload ảnh artist thất bại!", e);
        }
    }


}
