package com.kh.jde.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.kh.jde.common.responseData.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameNotFoundException.class) // 
	public ResponseEntity<ErrorResponse<String>> handleUsenameNotFound(UsernameNotFoundException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(CustomAuthenticationException.class)
	public ResponseEntity<ErrorResponse<String>> handleCustomAuthentication(CustomAuthenticationException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse<String>> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse<String>> handleIllegalState(IllegalStateException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class) // Controller - dto에서 Valid로 인해 에러응답할때 어떤 에러인지 정보를 담기위해서
	public ResponseEntity<ErrorResponse<String>> handleArgumentsNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getBindingResult().getFieldError().getDefaultMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class) // Controller - dto에서 Valid로 인해 에러응답할때 어떤 에러인지 정보를 담기위해서
	public ResponseEntity<ErrorResponse<String>> handleArgumentsNotValid(MethodArgumentTypeMismatchException e, HttpServletRequest request){
		return ErrorResponse.badRequest("파라미터에 잘못된 값이 전달되었습니다.", request.getRequestURI());
	}
	
	@ExceptionHandler(LogoutFailureException.class) 
	public ResponseEntity<ErrorResponse<String>> handleLoginFailure(LogoutFailureException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(MemberInfomationDuplicatedException.class) 
	public ResponseEntity<ErrorResponse<String>> handleMemberInfomationDuplicate(MemberInfomationDuplicatedException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(AccessDeniedException.class) 
	public ResponseEntity<ErrorResponse<String>> handleAccessDenied(AccessDeniedException e, HttpServletRequest request){
		return ErrorResponse.forbidden(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(DuplicateReportException.class) 
	public ResponseEntity<ErrorResponse<String>> handleDuplicateReport(DuplicateReportException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(UnexpectedSQLResponseException.class) 
	public ResponseEntity<ErrorResponse<String>> handleUnexpectedSQLResponse(UnexpectedSQLResponseException e, HttpServletRequest request){
		return ErrorResponse.internalServerError(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(PostNotFoundException.class) 
	public ResponseEntity<ErrorResponse<String>> handlePostNotFound(PostNotFoundException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(S3ServiceFailureException.class) 
	public ResponseEntity<ErrorResponse<String>> handlePostNotFound(S3ServiceFailureException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(AlreadyDeletedException.class) 
	public ResponseEntity<ErrorResponse<String>> handleAlreadyDeleted(AlreadyDeletedException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse<String>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
	    return ErrorResponse.badRequest("요청 형식(Content-Type)이 올바르지 않습니다. JSON 또는 form-data 설정을 확인하세요.", request.getRequestURI());
	}
	
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ErrorResponse<String>> handleMissingRequestPart(MissingServletRequestPartException e, HttpServletRequest request) {
	    return ErrorResponse.badRequest("필수 파트가 누락되었습니다: " + e.getRequestPartName(), request.getRequestURI());
	}
	
}
