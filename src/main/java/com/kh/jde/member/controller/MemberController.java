package com.kh.jde.member.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.admin.model.dto.DefaultImageDTO;
import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.ChangeDefaultProfileDTO;
import com.kh.jde.member.model.dto.ChangeNameDTO;
import com.kh.jde.member.model.dto.ChangeNicknameDTO;
import com.kh.jde.member.model.dto.ChangePasswordDTO;
import com.kh.jde.member.model.dto.ChangePhoneDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;
import com.kh.jde.member.model.dto.MemberWithdrawDTO;
import com.kh.jde.member.model.service.MemberInformationValidator;
import com.kh.jde.member.model.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final MemberInformationValidator duplicateValidator;
	
	@PostMapping
	public ResponseEntity<SuccessResponse<String>> signUp(@Valid @RequestBody MemberSignUpDTO member){
		// log.info("멤버 잘들어오나? : {}", member);
		memberService.signUp(member);
		return SuccessResponse.created("회원가입에 성공했습니다.");
	}
	
	@DeleteMapping
	public ResponseEntity<SuccessResponse<String>> withdraw(@Valid @RequestBody MemberWithdrawDTO member, @AuthenticationPrincipal CustomUserDetails user){
		memberService.withdraw(member.getPassword(), user);
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
	public ResponseEntity<SuccessResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDTO changePassword, @AuthenticationPrincipal CustomUserDetails user) {
	    memberService.changePassword(changePassword, user);
	    return SuccessResponse.ok("비밀번호가 변경되었습니다.");
	}
	
	@PatchMapping("/name")
	public ResponseEntity<SuccessResponse<String>> changeName(@Valid @RequestBody ChangeNameDTO ChangeName, @AuthenticationPrincipal CustomUserDetails user) {
	    memberService.changeName(ChangeName, user);
	    return SuccessResponse.ok("이름이 변경되었습니다.");
	}
	
	@PatchMapping("/nickname")
	public ResponseEntity<SuccessResponse<String>> changeNickname(@Valid @RequestBody ChangeNicknameDTO changeNickname, @AuthenticationPrincipal CustomUserDetails user) {
	    memberService.changeNickname(changeNickname, user);
	    return SuccessResponse.ok("닉네임이 변경되었습니다.");
	}
	
	@PatchMapping("/phone")
	public ResponseEntity<SuccessResponse<String>> changePhone(@Valid @RequestBody ChangePhoneDTO changePhone, @AuthenticationPrincipal CustomUserDetails user) {
	    memberService.changePhone(changePhone, user);
	    return SuccessResponse.ok("전화번호가 변경되었습니다.");
	}
	
	// 자신의 이미지 업로드로 변경
	@PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SuccessResponse<String>> updateProfileImage(
	        @RequestPart("password") String password,
	        @RequestPart("file") MultipartFile file,
	        @AuthenticationPrincipal CustomUserDetails user
	) {
	    String url = memberService.updateMyProfileImage(password, file, user);
	    return SuccessResponse.ok(url, "프로필 이미지가 변경되었습니다.");
	}
	
	@GetMapping("/profile-image/default")
	public ResponseEntity<SuccessResponse<List<DefaultImageDTO>>> getDefaultProfiles() {
		List<DefaultImageDTO> list = memberService.getDefaultProfiles();
		return SuccessResponse.ok(list, "기본 프로필 이미지 목록 조회 성공");
	}
	
	// 기본 이미지 선택(변경)
	@PatchMapping(value = "/profile-image/default", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<String>> changeProfileToDefault(
			@RequestBody ChangeDefaultProfileDTO defaultProfile, 
			@AuthenticationPrincipal CustomUserDetails user
	) {
	    String url = memberService.changeProfileToDefault(defaultProfile.getPassword(), defaultProfile.getFileNo(), user);
	    return SuccessResponse.ok(url, "기본 프로필 이미지로 변경되었습니다.");
	}
	
	// 이메일 중복여부 체크
	@GetMapping("email")
	public ResponseEntity<SuccessResponse<String>> emailDuplicateCheck(@RequestParam("email") 
																	   @NotBlank(message = "이메일을 입력해주세요.")
																	   @Email(message = "이메일 형식이 올바르지 않습니다.") String email){
		duplicateValidator.emailDuplicateCheck(email);
		return SuccessResponse.ok("사용 가능한 이메일입니다.");
	}
	
	// 닉네임 중복여부 체크
	@GetMapping("nickname")
	public ResponseEntity<SuccessResponse<String>> nicknameDuplicateCheck(@RequestParam("nickname")
																	   @NotBlank(message = "닉네임을 입력해주세요.")
																	   @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "닉네임은 영어, 한글만 사용 가능합니다.")
																	   @Size(min = 2, max = 10, message = "닉네임은 2글자 이상 10글자 이하만 사용할 수 있습니다.")
																	   String nickname){
		duplicateValidator.nicknameDuplicateCheck(nickname);
		return SuccessResponse.ok("사용 가능한 닉네임입니다.");
	}
	
	// 비밀번호 검증 체크
	@PostMapping("/password/verify")
	public ResponseEntity<SuccessResponse<String>> verifyPassword(
	        @Valid @RequestBody MemberWithdrawDTO dto, @AuthenticationPrincipal CustomUserDetails user
	) {
	    memberService.verifyPassword(dto.getPassword(), user);
	    return SuccessResponse.ok("비밀번호 확인 성공");
	}

}
