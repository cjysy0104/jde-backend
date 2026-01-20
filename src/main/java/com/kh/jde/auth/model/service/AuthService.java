package com.kh.jde.auth.model.service;

import com.kh.jde.auth.model.dto.AccessTokenResponseDTO;
import com.kh.jde.auth.model.dto.LoginResponseDTO;
import com.kh.jde.member.model.dto.MemberLoginDTO;
import com.kh.jde.member.model.dto.MemberLogoutDTO;

public interface AuthService {
	
	LoginResponseDTO  login(MemberLoginDTO member);
	
	void logout(MemberLogoutDTO member);
	
	AccessTokenResponseDTO reissueAccessToken(String refreshToken);

}
