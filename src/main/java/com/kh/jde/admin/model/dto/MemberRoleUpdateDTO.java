package com.kh.jde.admin.model.dto;

import jakarta.validation.constraints.NotBlank;
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
public class MemberRoleUpdateDTO {
	
	private Long memberNo; // 변경할 회원 번호
	private Long currentMemberNo; // 현재 로그인한 회원 번호
	
	@NotBlank(message = "권한은 필수입니다.")
	private String role; // ROLE_USER, ROLE_ADMIN
}
