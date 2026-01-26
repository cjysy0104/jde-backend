package com.kh.jde.configuration.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.token.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	// 필터의 주요로직을 구현하는 메서드, 요청이 들어올 때마다 호출됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// log.info("진짜로 요청이 들어올 때마다 요친구가 호출되는지 확인");
		
		String uri = request.getRequestURI();
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 아래 요청은 다 통과
	    if (authorization == null || !authorization.startsWith("Bearer ")) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		
		// 토큰 검증
		String token = authorization.split(" ")[1];

		try {
			Claims claims = jwtUtil.parseJwt(token);
			String username = claims.getSubject();

			CustomUserDetails user = (CustomUserDetails)userDetailsService.loadUserByUsername(username);
			
            if ("N".equals(user.getStatus())) {
                // log.info("정지계정? : {}", username);
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 접근권한으로 인해 거절이므로 403 반환
                response.getWriter().write("정지된 계정입니다.");
                return;
            }
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch(ExpiredJwtException e) {
			log.info("토큰의 유효기간 만료");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("토큰만료");
			return;
		} catch(JwtException e) {
			log.info("서버에서 만들어진 토큰이 아님");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("유효하지 않은 토큰입니다.");
			return;
		}
		filterChain.doFilter(request, response);
	}
	
	
	
	
}