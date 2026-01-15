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
public class MemberWithdrawDTO {
	
	
	
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$", message = "비밀번호는 영어, 숫자, 특수문자가 최소 1개 이상씩 필요합니다.")
	@Size(min = 8, max = 16, message = "비밀번호 값은 8글자 이상 16글자 이하만 사용할 수 있습니다.")
	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")	
	private String password;

	private String status;

}
