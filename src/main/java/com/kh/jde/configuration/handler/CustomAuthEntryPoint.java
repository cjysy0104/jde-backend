package com.kh.jde.configuration.handler;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.jde.common.responseData.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        String msg = (String) request.getAttribute("auth_error_message");
        if (msg == null) msg = "인증정보가 없습니다. 로그인 하시지 않았다면 로그인 해주세요.";

        ErrorResponse<Object> body = ErrorResponse.builder()
                .status(401)
                .success(false)
                .message(msg)
                .path(getOriginalPath(request))
                .timeStamp(LocalDateTime.now())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
    
    // 
    private String getOriginalPath(HttpServletRequest request) {
        Object uri = request.getAttribute("jakarta.servlet.error.request_uri");
        return (uri != null) ? uri.toString() : request.getRequestURI();
    }

}
