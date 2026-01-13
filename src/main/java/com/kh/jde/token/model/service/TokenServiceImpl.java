package com.kh.jde.token.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.jde.exception.CustomAuthenticationException;
import com.kh.jde.token.model.dao.TokenMapper;
import com.kh.jde.token.model.vo.RefreshToken;
import com.kh.jde.token.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	
	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;

	public Map<String, String> generateToken(String username) {

		
		Map<String, String> tokens = createTokens(username);

		saveToken(tokens.get("refreshToken"), username);

		
		return tokens;
	}
	
	private Map<String, String> createTokens(String username){
		String accessToken = tokenUtil.getAccessToken(username);
		String refreshToken = tokenUtil.getRefreshToken(username);
		
		Map<String, String> tokens = new HashMap();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		return tokens;
	}
	
	private void saveToken(String refreshToken, String username) {
		RefreshToken token = RefreshToken.builder()
												   .token(refreshToken)
										           .username(username)
				   								   .expiration(System.currentTimeMillis() + 3600000L * 72)
				   								   .build();

		tokenMapper.saveToken(token);
	}
	
	public Map<String, String> validateToken(String refreshToken) {
		RefreshToken token = tokenMapper.findByToken(refreshToken);
		if(token == null || token.getExpiration() < System.currentTimeMillis()) {
			throw new CustomAuthenticationException("유효하지 않은 요청입니다.");
		}
		Claims claims = tokenUtil.parseJwt(refreshToken); 
		String username = claims.getSubject();
		return createTokens(username);
	}
	

}
