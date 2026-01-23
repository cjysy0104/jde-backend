package com.kh.jde.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.dto.AccessTokenResponseDTO;
import com.kh.jde.auth.model.dto.LoginResponseDTO;
import com.kh.jde.auth.model.dto.RefreshTokenRequestDTO;
import com.kh.jde.auth.model.service.AuthService;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.member.model.dto.MemberLoginDTO;
import com.kh.jde.member.model.dto.MemberLogoutDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	// private final TokenService tokenService;
	
	@PostMapping("login")
	public ResponseEntity<SuccessResponse<LoginResponseDTO>> login(@Valid @RequestBody MemberLoginDTO member){
		LoginResponseDTO loginResponse = authService.login(member);
		return SuccessResponse.ok(loginResponse, "로그인에 성공했습니다.");
	}
	
	@PostMapping("logout")
	public ResponseEntity<SuccessResponse<String>> logout(@Valid @RequestBody MemberLogoutDTO member){
		authService.logout(member);
		return SuccessResponse.ok("로그아웃 되었습니다.");
	}
	
	@PostMapping("refresh")
	public ResponseEntity<SuccessResponse<AccessTokenResponseDTO>> reissueAccessToken(@RequestBody RefreshTokenRequestDTO refreshTokenDTO){
		AccessTokenResponseDTO accessToken = authService.reissueAccessToken(refreshTokenDTO.getRefreshToken());
		return SuccessResponse.ok(accessToken, "로그인 연장 성공");
	}
	
}