package com.kh.jde.admin.model.dto;

import java.sql.Date;

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
public class MemberListDTO {
	
	private Long memberNo;
	private String email; // 마스킹됨
	private String memberName; // 마스킹됨
	private String nickname;
	private String phone; // 마스킹됨
	private Date enrollDate;
	private String status;
	private String role;
}
