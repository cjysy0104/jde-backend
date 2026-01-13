package com.kh.jde.token.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.member.model.dto.MemberLogoutDTO;
import com.kh.jde.token.model.vo.RefreshToken;

@Mapper
public interface TokenMapper {
	
	int saveToken(RefreshToken token);
	
	void deleteToken(String memberId);

	RefreshToken findByToken(String refreshToken);
	
	int deleteTokenForLogout(MemberLogoutDTO member);
	
}
