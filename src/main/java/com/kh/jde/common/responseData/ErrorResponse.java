package com.kh.jde.common.responseData;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class ErrorResponse<T> {
	
	private int status;
	private boolean success;
	private String message;
	private String path;
	private LocalDateTime timeStamp;
	
	private ErrorResponse(int status, boolean success, String message, String path, LocalDateTime timeStamp) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.path = path;
		this.timeStamp = timeStamp;
	}
	
	// 400 BAD_REQUEST 잘못된 요청
	public static <T> ResponseEntity<ErrorResponse<T>> badRequest(String message, String path){
		return ResponseEntity.badRequest().body(new ErrorResponse(400, false, message, path, LocalDateTime.now()));
	}
	
	// 401 Unauthorized 미인증
	public static <T> ResponseEntity<ErrorResponse<T>> unathorized(String message, String path){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, false, message, path, LocalDateTime.now()));
	}
	
	// 403 Unauthorized 미인가
	public static <T> ResponseEntity<ErrorResponse<T>> forbidden(String message, String path){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, false, message, path, LocalDateTime.now()));
	}
	
	// 404 Not Found 요청한 리소스를 찾을수 없음.
	// 클라이언트는 프론트엔드에서 처리한 응답을 받는게 정상이나, 악의적인 사용자로 인해 잘못된 요청이 올 수도 있으니 방어적 코딩 해두기.
	public static <T> ResponseEntity<ErrorResponse<T>> notFound(String message, String path){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, false, message, path, LocalDateTime.now()));
	}
	
	// 500 Internal Server Error 서버측의 문제
	public static <T> ResponseEntity<ErrorResponse<T>> internalServerError(String message, String path){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, false, message, path, LocalDateTime.now()));
	}
	

}