package com.kh.jde.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.member.model.dto.MemberLoginDTO;
import com.kh.jde.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	int signUp(MemberVO member);
	
	MemberLoginDTO loadUser(String email);
	
	int countByNickname(String memberId);
	int countByPhone(String phone);
	int countByEmail(String email);

}
