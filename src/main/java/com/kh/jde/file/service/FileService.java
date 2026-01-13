package com.kh.jde.file.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.file.FileRenamePolicy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {
	
	private final FileRenamePolicy fileRenamePolicy;
	private final Path fileLocation;
	
	public FileService(FileRenamePolicy fileRenamePolicy) {
		this.fileRenamePolicy = fileRenamePolicy;
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileLocation);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장소 생성 실패", e);
		}
	}
	
	/**
	 * 파일을 저장 changeName 반환
	 */
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
	
}
