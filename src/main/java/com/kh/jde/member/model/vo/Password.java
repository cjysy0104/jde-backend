package com.kh.jde.member.model.vo;

import org.springframework.security.crypto.password.PasswordEncoder;

public record Password(String password) {
	
	// 비밀번호 암호화
	public static Password toEncoded(String plainPassword, PasswordEncoder encoder) {
		return new Password(encoder.encode(plainPassword));
	}
	
	// 비밀번호 검증
	public static boolean matches(String rawPassword, String encodedPassword, PasswordEncoder encoder) {
		return encoder.matches(rawPassword, encodedPassword);
	}
	
}
