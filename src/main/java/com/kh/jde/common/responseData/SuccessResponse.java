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
public class SuccessResponse<T> {

	private int status;
	private boolean success;
	private String message;
	private Object data;
	private LocalDateTime timeStamp;
	
	private SuccessResponse(int status, boolean success, String message, Object data, LocalDateTime timeStamp) {
		this.status = status;
		this.success = success;
		this.message = message;
		this.data = data;
		this.timeStamp = timeStamp;
	}
	
	// 200 OK / GET방식 / 응답메세지 따로 없을 경우 / 일단 안쓰는 걸로
	/*
	public static <T> ResponseEntity<SuccessResponse<T>> ok(Object data){
		return ResponseEntity.ok(new SuccessResponse(200, true, null, data, LocalDateTime.now()));
	}
	*/
	// 200 OK / GET방식 / 응답메세지 있을 경우
	public static <T> ResponseEntity<SuccessResponse<T>> ok(Object data, String message){
		return ResponseEntity.ok(new SuccessResponse(200, true, message, data, LocalDateTime.now()));
	}
	
	// 201 CREATED / POST방식 / 새로운 리소스가 생성되었는데 응답데이터는 보내지 않는 경우
	public static <T> ResponseEntity<SuccessResponse<T>> created(String message){
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<T>(201, true, message, null, LocalDateTime.now()));
	}
	// 201 CREATED / POST방식 / 새로운 리소스가 생성될 경우
	public static <T> ResponseEntity<SuccessResponse<T>> created(Object data, String message){
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<T>(201, true, message, data, LocalDateTime.now()));
	}
	
	
	// 204 NO_CONTENT / DELETE, PUT, PATCH / 전달 메세지 있음
	public static <T> ResponseEntity<SuccessResponse<T>> noContent(String message){
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse(204, true, message, null, LocalDateTime.now()));
	}
	
	// 204 NO_CONTENT / DELETE, PUT, PATCH / 전달 메세지 없음, 그 외 위와 동일
	public static <T> ResponseEntity<SuccessResponse<T>> noContent(){
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse(204, true, null, null, LocalDateTime.now()));
	}
}