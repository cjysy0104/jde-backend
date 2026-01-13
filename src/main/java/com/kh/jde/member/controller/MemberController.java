package com.kh.jde.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.member.model.dto.MemberSignUpDTO;
import com.kh.jde.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping
	public ResponseEntity<SuccessResponse<String>> signUp(@Valid @RequestBody MemberSignUpDTO member){
		// log.info("멤버 잘들어오나? : {}", member);
		memberService.signUp(member);
		return SuccessResponse.created("회원가입에 성공했습니다.");
	}
	
	
	
}
