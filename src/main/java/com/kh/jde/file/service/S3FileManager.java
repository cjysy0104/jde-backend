package com.kh.jde.file.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.exception.S3ServiceFailureException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileManager {

	private final S3Service s3Service;
	
	public List<String> createFiles(String baseDir, Long No, List<MultipartFile> images, int maxAttempt) {
		List<String> uploadUrl = new ArrayList<>();
		
		try {
			if (images != null) {
				for (MultipartFile f : images) {
					if (f == null || f.isEmpty()) continue;
					
					String url = s3Service.fileSave(f, baseDir + "/" + No);
					uploadUrl.add(url);
				}
			}
		} catch (Exception e) {
			for (String url : uploadUrl) {
				try { 
					s3Service.deleteFile(url);
				} catch (Exception ignore) {
					deleteWithRetry(url, maxAttempt);
				}
			}
			throw e;
		}
		
		return uploadUrl;
	}
	
	public void deleteUrls(List<String> urls, int maxAttempts) {
		for(String url : urls) {
			try {
				s3Service.deleteFile(url);
			} catch (Exception e) {
				deleteWithRetry(url, maxAttempts);
			}
		}
	}
	
    public void deleteWithRetry(String url, int maxAttempts) {
        if (url == null || url.isBlank()) return;

        int attempt = 0;
        while (true) {
            try {
                s3Service.deleteFile(url);
                return;
            } catch (Exception e) {
                attempt++;
                log.warn("S3 delete failed attempt {}/{} url={}", attempt, maxAttempts, url, e);

                if (attempt >= maxAttempts) {
                    throw new S3ServiceFailureException(url + ": S3 파일 삭제 재시도 초과");
                }

                backoff(attempt);
            }
        }
    }

    public void deleteAllWithRetry(List<String> urls, int maxAttempts) {
        if (urls == null || urls.isEmpty()) return;
        
        for (String url : urls) deleteWithRetry(url, maxAttempts);
    }

    /** 업로드 중 예외 발생 시 되돌림: 실패해도 최대한 지우고 로그 남김 */
    public void rollbackUploadedFiles(List<String> uploadedUrls, int maxAttempts) {
        if (uploadedUrls == null || uploadedUrls.isEmpty()) return;

        for (String url : uploadedUrls) {
            try {
                deleteWithRetry(url, maxAttempts);
            } catch (Exception e) {
                log.error("Rollback delete failed url={}", url, e);
            }
        }
    }

    private void backoff(int attempt) {
        long sleepMs = Math.min(200L * attempt, 1000L);
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
