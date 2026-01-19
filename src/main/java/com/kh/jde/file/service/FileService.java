package com.kh.jde.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.kh.jde.file.FileRenamePolicy;
import com.kh.jde.file.dao.FileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {
	
	private final FileRenamePolicy fileRenamePolicy;
	private final Path fileLocation;
	private final FileMapper fileMapper;
	
	
	public FileService(FileRenamePolicy fileRenamePolicy, FileMapper fileMapper) {
		this.fileRenamePolicy = fileRenamePolicy;
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileLocation);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장소 생성 실패", e);
		}
		this.fileMapper = fileMapper;
	}
	
	public String getDefaultImage() {
	    List<String> urls = fileMapper.getDefaultImage();

	    if (urls.isEmpty()) {
	        throw new IllegalStateException("기본 프로필 이미지가 없습니다.");
	    }

	    int randomIndex = ThreadLocalRandom.current().nextInt(urls.size());
	    return urls.get(randomIndex);
	}
	
	
	/**
	 * 파일을 저장 changeName 반환
	 */
	/*
	public String store(MultipartFile file) {
		
		String originalFilename = file.getOriginalFilename();

		File originFile = new File(originalFilename);
		
		File renamedFile = fileRenamePolicy.rename(originFile);
		String changeName = renamedFile.getName();
		
		Path targetLocation = this.fileLocation.resolve(changeName);
		
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return changeName;

		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패: " + e.getMessage());
		}
	}
	*/
	
}
