package com.kh.jde.token.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshToken {

	private int tokenNo;
	private String token;
	private String username;
	private Long expiration;
	private Date regDate;
}
