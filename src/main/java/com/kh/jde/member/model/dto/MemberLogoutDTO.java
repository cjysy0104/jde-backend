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
public class MemberLogoutDTO {

	@Email(message="이메일 형식이 올바르지 않습니다.")
	private String email;
	
	@NotBlank(message = "적합하지 않은 요청입니다.")
	private String refreshToken;

}
