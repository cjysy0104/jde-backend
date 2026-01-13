package com.kh.jde.member.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class MemberVO {
	
	private Long memberNo;
	private String email;
	private String password;
	private String memberName;
	private String nickname;
	private String phone;
	private Date enrollDate;
	private String status;
	private String role;

}
