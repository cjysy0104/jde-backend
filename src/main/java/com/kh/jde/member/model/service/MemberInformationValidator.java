package com.kh.jde.member.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.jde.exception.MemberInfomationDuplicatedException;
import com.kh.jde.member.model.dao.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberInformationValidator {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	public void MemberInfomationDuplicateCheck(String nickname, String email, String phone) {
		int nicknameCount = memberMapper.countByNickname(nickname);
		if(1 <= nicknameCount) {
			throw new MemberInfomationDuplicatedException("이미 존재하는 닉네임입니다.");
		}
		
		int EamilCount = memberMapper.countByEmail(email);
		if(1 <= EamilCount) {
			throw new MemberInfomationDuplicatedException("이미 존재하는 이메일입니다.");
		}
		
		int PhoneCount = memberMapper.countByPhone(phone);
		if(1 <= PhoneCount) {
			throw new MemberInfomationDuplicatedException("이미 존재하는 휴대전화 번호입니다.");
		} 	
	}
	
	
}
