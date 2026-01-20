package com.kh.jde.auth.model.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.auth.model.dto.AccessTokenResponseDTO;
import com.kh.jde.auth.model.dto.LoginResponseDTO;
import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.exception.CustomAuthenticationException;
import com.kh.jde.exception.LogoutFailureException;
import com.kh.jde.member.model.dto.MemberLoginDTO;
import com.kh.jde.member.model.dto.MemberLogoutDTO;
import com.kh.jde.token.model.dao.TokenMapper;
import com.kh.jde.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final TokenMapper tokenMapper;

	@Override
	public LoginResponseDTO  login(MemberLoginDTO member) {
		
		Authentication auth = null;
		try {
			auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword()));
		} catch(AuthenticationException e) {
			throw new CustomAuthenticationException("비밀번호 미일치");
		}
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		Map<String, String> tokens = tokenService.generateToken(user.getUsername());
		// 토큰 정보에 추가로 앞단에서 필요한 정보들을 담아서 같이 보내준다.
		
		LoginResponseDTO loginResponse = LoginResponseDTO.builder()
														 .accessToken(tokens.get("accessToken"))
														 .refreshToken(tokens.get("refreshToken"))
														 .email(user.getUsername())
														 .memberName(user.getMemberName())
														 .nickname(user.getNickname())
														 .phone(user.getPhone())
														 .memberNo(user.getMemberNo())
														 .enrollDate(user.getEnrollDate())
														 .role(user.getRole())
														 .fileUrl(user.getFileUrl())
														 .status(user.getStatus())
														 .build();
		return loginResponse;
	}
	
	
	@Override
	@Transactional
	public void logout(MemberLogoutDTO member) {
		
		int result = tokenMapper.deleteTokenForLogout(member);
		
		if(result == 1) {
			return;
		} else {
			throw new LogoutFailureException("로그아웃 오류 발생, 관리자에게 문의해주세요.");
		}
	}


	@Override
	public AccessTokenResponseDTO reissueAccessToken(String refreshToken) {
		String newAccessToken = tokenService.validateToken(refreshToken);
		AccessTokenResponseDTO accessToken = AccessTokenResponseDTO.builder()
																	  .accessToken(newAccessToken)
																	  .build();
		return accessToken;
	}
	

}
