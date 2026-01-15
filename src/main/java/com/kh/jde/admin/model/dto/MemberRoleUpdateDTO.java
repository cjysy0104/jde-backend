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
	
	@NotBlank(message = "권한은 필수입니다.")
	private String role; // ROLE_USER, ROLE_ADMIN
}
