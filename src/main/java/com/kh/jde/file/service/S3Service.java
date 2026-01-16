package com.kh.jde.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.file.FileRenamePolicy;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
public class S3Service {
	
	private final S3Client s3Client;
	private final FileRenamePolicy fileRenamePolicy;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	// 업로드 메소드 
	public String fileSave(MultipartFile file, String S3DirectoryName) {
		
		// 이미지 파일이 png, jpg, jpeg가 아니면 예외발생
		String contentType = file.getContentType();
		if (contentType == null || !(contentType.equals("image/png")
				|| contentType.equals("image/jpg")
				|| contentType.equals("image/jpeg"))) {
		    throw new IllegalArgumentException("허용되지 않은 파일 형식입니다. (png, jpg, jpeg만 가능)");
		}
		
		// 서비스에서 넘겨준 폴더경로와 랜덤생성한 파일명 합치기
		String key = new StringBuilder()
		        .append(S3DirectoryName)
		        .append("/")
		        .append(fileRenamePolicy.rename())
		        .toString();
		
		// S3에 업로드
		PutObjectRequest request = PutObjectRequest.builder()
												   .bucket(bucketName)
												   .key(key)
												   .contentType(file.getContentType())
												   .build();
		
		try {
			s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (S3Exception e) {
			e.printStackTrace(); // 예외들 다 throw해야함
		} catch (AwsServiceException e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String filePath = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + changeName;
		
		return filePath;
		
	}

	public void deleteFile(String filePath) {
		// 이미지파일 경로에서 마지막 /뒤의 파일명만 가져와야함
		String objectKey = getObjectKeyFromUrl(filePath);
	
		try {
		DeleteObjectRequest request = DeleteObjectRequest.builder()
				 										 .bucket(bucketName)
				 										 .key(objectKey)
				 										 .build();
		s3Client.deleteObject(request);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("S3파일 삭제 실패: " + e.getMessage());
		}
	}
	
	public String getObjectKeyFromUrl(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		try {
			URL url = new URL(filePath);
			String path = url.getPath();
			return path.substring(1);
		} catch (MalformedURLException e){
			e.printStackTrace();
		}
		throw new RuntimeException("파일 형식이 올바르지 않습니다.");
	}

}