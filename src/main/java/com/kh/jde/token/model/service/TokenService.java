package com.kh.jde.token.model.service;

import java.util.Map;

public interface TokenService {
	
	Map<String, String> generateToken(String username);
	
	Map<String, String> validateToken(String refreshToken);

}
