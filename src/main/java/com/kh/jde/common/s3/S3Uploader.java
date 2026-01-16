package com.kh.jde.common.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadProfileImage(MultipartFile file, Long memberNo) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String ext = guessExt(contentType);
        String key = "profile/" + memberNo + "/" + UUID.randomUUID() + "." + ext;

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(req, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 버킷이 퍼블릭이거나 CloudFront 붙인 경우에만 바로 접근 가능
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }

    private String guessExt(String contentType) {
        return switch (contentType) {
            case MediaType.IMAGE_PNG_VALUE -> "png";
            case MediaType.IMAGE_GIF_VALUE -> "gif";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
    }
}