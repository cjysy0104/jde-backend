package com.kh.jde.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.ChangeNameDTO;
import com.kh.jde.member.model.dto.ChangePasswordDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;
import com.kh.jde.member.model.dto.MemberWithdrawDTO;
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
	
	@DeleteMapping
	public ResponseEntity<SuccessResponse<String>> withdraw(@Valid @RequestBody MemberWithdrawDTO member){
		memberService.withdraw(member.getPassword());
		return SuccessResponse.ok("탈퇴 되었습니다.");
	}
	
	@GetMapping
	public ResponseEntity<SuccessResponse<List<CaptainDTO>>> getCaptainList(){
		List<CaptainDTO> captains = memberService.getCaptainList();
		
		String message = captains.isEmpty()
		        			? "아직 미식대장이 없습니다."
		        			: "미식대장 조회에 성공했습니다.";

		return SuccessResponse.ok(captains, message);
	}
	
	@PatchMapping("/password")
	public ResponseEntity<SuccessResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDTO changePassword) {
	    memberService.changePassword(changePassword);
	    return SuccessResponse.ok("비밀번호가 변경되었습니다.");
	}
	
	@PatchMapping("/name")
	public ResponseEntity<SuccessResponse<String>> changeName(@Valid @RequestBody ChangeNameDTO ChangeName) {
	    memberService.changeName(ChangeName);
	    return SuccessResponse.ok("이름이 변경되었습니다.");
	}
	
}
