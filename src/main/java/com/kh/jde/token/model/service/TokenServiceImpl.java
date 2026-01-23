package com.kh.jde.token.model.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.exception.CustomAuthenticationException;
import com.kh.jde.token.model.dao.TokenMapper;
import com.kh.jde.token.model.vo.RefreshToken;
import com.kh.jde.token.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	
	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;

	@Value("${jwt.refresh-token-expiration}")
	private Duration refreshTokenExpiration;
	
	public Map<String, String> generateToken(String username) {
		
		Map<String, String> tokens = createTokens(username);

		saveToken(tokens.get("refreshToken"), username);

		
		return tokens;
	}
	
	// AccessToken, RefreshToken 둘 다 만들기
	private Map<String, String> createTokens(String username){
		String accessToken = tokenUtil.getAccessToken(username);
		String refreshToken = tokenUtil.getRefreshToken(username);
		
		Map<String, String> tokens = new HashMap();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		return tokens;
	}
	
	private void saveToken(String refreshToken, String username) {
		long expirationMillis = System.currentTimeMillis() + refreshTokenExpiration.toMillis();
		
		RefreshToken token = RefreshToken.builder()
										 .token(refreshToken)
										 .username(username)
				   						 .expiration(expirationMillis)
				   						 .build();
		tokenMapper.saveToken(token);
	}
	
	@Transactional
	public String validateToken(String refreshToken) {
		RefreshToken token = tokenMapper.findByToken(refreshToken);
		// DB에서 토큰 조회해서 검증하기
		if(token == null) {
			throw new CustomAuthenticationException("존재하지 않은 토큰입니다.");
		}
		if(token.getExpiration() < System.currentTimeMillis()) {
			tokenMapper.deleteTokenByRefreshToken(refreshToken); // 유효기간 지난 토큰은 삭제하기
			throw new CustomAuthenticationException("유효기간이 지난 토큰입니다. 재 로그인 하세요.");
		}
		
		// 토큰 파싱해서 서버에서 만든 토큰인지 확인
		Claims claims = null;
		try {
			claims = tokenUtil.parseJwt(refreshToken); 
		} catch(JwtException e) {
			throw new CustomAuthenticationException("서버에서 만들어진 토큰이 아닙니다.");
		}
		
		String username = claims.getSubject();
		String accessToken = createAccessToken(username);
		return accessToken;
	}

	// AccessToken만 만들기
	private String createAccessToken(String username){
		String accessToken = tokenUtil.getAccessToken(username);
		
		return accessToken;
	}

}
