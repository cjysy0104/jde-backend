package com.kh.jde.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberSignUpDTO {
	
	@Email(message="이메일 형식이 올바르지 않습니다.")
	private String email;
	
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$", message = "비밀번호는 영어, 숫자, 특수문자가 최소 1개 이상씩 필요합니다.")
	@Size(min = 8, max = 16, message = "비밀번호 값은 8글자 이상 16글자 이하만 사용할 수 있습니다.")
	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")	
	private String password;
	
	@Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "이름은 영어, 한글만 사용 가능합니다.")
	@Size(min = 2, max = 40, message = "이름은 2글자 이상 40글자 이하만 사용할 수 있습니다.")
	@NotBlank(message = "이름은 필수 입력사항입니다.")	
	private String memberName;
	
	@Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "닉네임은 영어, 한글만 사용 가능합니다.")
	@Size(min = 2, max = 10, message = "닉네임은 2글자 이상 10글자 이하만 사용할 수 있습니다.")
	@NotBlank(message = "닉네임은 필수 입력사항입니다.")	
	private String nickname;
	
	@Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	@NotBlank(message = "전화번호는 필수 입력사항입니다.")	
	private String phone;
	

}
