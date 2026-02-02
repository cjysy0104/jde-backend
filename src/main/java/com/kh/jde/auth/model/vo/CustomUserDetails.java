package com.kh.jde.auth.model.vo;

import java.sql.Date;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Value;

@Value // AllArgsConstructor, Getter, ToString, equals, hashCode
@Builder
public class CustomUserDetails implements UserDetails {
	
	private String username; 
	private String password;
	private String memberName;
	private Collection<? extends GrantedAuthority> authorities;
	
	private Long memberNo;
	private String email;
	private String nickname;
	private String phone;
	private Date enrollDate;
	private String status;
	private String role;
	private String fileUrl;

	
}
