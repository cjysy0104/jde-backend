package com.kh.jde.token.model.service;

import java.util.Map;

public interface TokenService {
	
	Map<String, String> generateToken(String username);
	
	String validateToken(String refreshToken);

}
