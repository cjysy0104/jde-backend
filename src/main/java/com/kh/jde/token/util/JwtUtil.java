package com.kh.jde.token.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	// 토큰을 만들어내는 기능들을 가지고 있을 클래스
	// 문자열값이 필요함 > 필드로 등록하면 보안상 위험 > 별도의 파일에 따로 빼서 씀 
	// 여기선 기본 .yml 파일에 기재 (보통은 별개의 파일 생성), 이 파일들은 gitignore
	// 이렇게 애플리케이션 설정파일에 정의된 속성의 값들을 클래스 내부에서 가져다 쓰고 싶다
	@Value("${jwt.secret}") // 롬복이아닌 beans의 애노테이션
	private String secretKey;
	private SecretKey key;
	
	// yml에서 주입 받을 만료시간
	@Value("${jwt.access-token-expiration}")
	private Duration accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}")
	private Duration refreshTokenExpiration;
	
	@PostConstruct
	public void init() {
		byte[] arr = Base64.getDecoder().decode(secretKey);
		this.key = Keys.hmacShaKeyFor(arr);
	}
	
	public String getAccessToken(String username) {
		return getToken(username, accessTokenExpiration);
	}
	
	public String getRefreshToken(String username) {
		return getToken(username, refreshTokenExpiration);
	}
	
	private String getToken(String username, Duration expiration) {
		Instant now = Instant.now();
		
		return Jwts.builder().subject(username) // 사용자 아이디
	 			 .issuedAt(Date.from(now)) // 발급일
	 			 .expiration(Date.from(now.plus(expiration))) // 만료일 
	 			 .signWith(key) // 서명
	 			 .compact();
	}
	
	public Claims parseJwt(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

}
