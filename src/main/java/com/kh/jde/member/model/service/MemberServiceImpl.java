package com.kh.jde.member.model.service;

import java.sql.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.jde.member.model.dao.MemberMapper;
import com.kh.jde.member.model.dto.MemberSignUpDTO;
import com.kh.jde.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final MemberInformationValidator miv;
	
	@Override
	public void signUp(MemberSignUpDTO member) {
		miv.MemberInfomationDuplicateCheck(member.getNickname(), member.getEmail(), member.getPhone());
		// 닉네임, 이메일, 휴대폰 중복 체크
		
		// 비밀번호 암호화
		String encodedPwd = passwordEncoder.encode(member.getPassword());
		
		
		// 현재 시간을 sql.Date로 변환
        Date currentDate = new Date(System.currentTimeMillis());
		
		MemberVO signUpMember = MemberVO.builder()
										 .email(member.getEmail())
										 .nickname(member.getNickname())
										 .password(passwordEncoder.encode(member.getPassword()))
				                         .memberName(member.getMemberName())
				                         .phone(member.getPhone())
				                         .build();

		
		// 매퍼 호출
		memberMapper.signUp(signUpMember);
		// log.info("사용자 등록 성공 : {}", signUpMember);
		
	}

}
