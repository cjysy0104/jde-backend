package com.kh.jde.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileRenamePolicy {

    public String newFileName(MultipartFile originFile) {

        // 원본 파일명
    	String originName = originFile.getOriginalFilename();
    	
    	// 확장자 
        String ext = "";

        // 1. 파일명 기반 확장자
        if (originName != null) {
            int dotIndex = originName.lastIndexOf(".");
            if (dotIndex != -1 && dotIndex < originName.length() - 1) {
                ext = originName.substring(dotIndex);
            }
        }

        // 2. 확장자 없으면 Content-Type 기반 보완
        if (ext.isBlank()) {
            String contentType = originFile.getContentType();
            if ("image/png".equals(contentType)) ext = ".png";
            else if ("image/jpeg".equals(contentType)) ext = ".jpg";
        }

        // 2. 년월일시분초
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 3. UUID (하이픈 제거)
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 4. 최종 파일명
        String changeName = "JDE_" + currentTime + "_" + uuid  + ext;

        return changeName;
    }
    
    public File rename(File originFile) {

        // 원본 파일명
        String originName = originFile.getName();

        // 1. 원본파일의 확장자 
        String ext = originName.substring(originName.lastIndexOf("."));

        // 2. 년월일시분초
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 3. UUID (하이픈 제거)
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 4. 최종 파일명
        String changeName = "JDE_" + currentTime + "_" + uuid + ext;

        return new File(originFile.getParent(), changeName);
    }
}
