package com.kh.jde.exception;

import java.security.InvalidParameterException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
	
	@ExceptionHandler(MethodArgumentNotValidException.class) // Controller - dto에서 Valid로 인해 에러응답할때 어떤 에러인지 정보를 담기위해서
	public ResponseEntity<ErrorResponse<String>> handleArgumentsNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
		return ErrorResponse.badRequest(e.getBindingResult().getFieldError().getDefaultMessage(), request.getRequestURI());
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
		return ErrorResponse.forbidden(e.getMessage(), request.getRequestURI());
	}
	
}
