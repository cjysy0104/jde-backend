package com.kh.jde.member.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.MemberLoginDTO;
import com.kh.jde.member.model.vo.MemberFileVO;
import com.kh.jde.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	int signUp(MemberVO member);
	
	MemberLoginDTO loadUser(String email);
	
	int countByNickname(String nickname); 
	int countByPhone(String phone);
	int countByEmail(String email);
	
	int withdrawByPassword(String email);
	
	List<CaptainDTO> getCaptainList();
	
	String findPasswordByEmail(String email);
	
	int updatePasswordByEmail(MemberVO member);
	int updateNameByEmail(MemberVO member);
	int updateNicknameByEmail(MemberVO member);
	int updatePhoneByEmail(MemberVO member);

    int upsertProfileImage(MemberFileVO vo);
    
}
