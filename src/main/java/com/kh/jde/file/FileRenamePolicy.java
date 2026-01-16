package com.kh.jde.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class FileRenamePolicy {
	
    public File rename(File originFile) {

        // 원본 파일명
        String originName = originFile.getName();

        //1. 원본파일의 확장자 
        String ext = originName.substring(originName.lastIndexOf("."));

        // 2. 년월일시분초
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 3. 랜덤 숫자
        int randomNo = (int)(Math.random() * 900) + 100;

        // 4. 최종 파일명
        String changeName = "JDE_" + currentTime + "_" + randomNo + ext;

        return new File(originFile.getParent(), changeName);
    }

}
