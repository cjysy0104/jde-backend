package com.kh.jde.member.model.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.exception.CustomAuthenticationException;
import com.kh.jde.exception.UnexpectedSQLResponseException;
import com.kh.jde.member.model.dao.MemberMapper;
import com.kh.jde.member.model.dto.MemberSignUpDTO;
import com.kh.jde.member.model.vo.MemberVO;
import com.kh.jde.member.model.vo.Password;
import com.kh.jde.token.model.dao.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final MemberInformationValidator miv;
	private final TokenMapper tokenMapper;
	
	@Override
	public void signUp(MemberSignUpDTO member) {
		miv.MemberInfomationDuplicateCheck(member.getNickname(), member.getEmail(), member.getPhone());
		// 닉네임, 이메일, 휴대폰 중복 체크
		
		// 비밀번호 암호화
		// String encodedPwd = passwordEncoder.encode(member.getPassword());
		Password password = Password.toEncoded(member.getPassword(), passwordEncoder);
		
		// 현재 시간을 sql.Date로 변환
        // Date currentDate = new Date(System.currentTimeMillis());
		
		MemberVO signUpMember = MemberVO.builder()
										 .email(member.getEmail())
										 .nickname(member.getNickname())
										 .password(password.password())
				                         .memberName(member.getMemberName())
				                         .phone(member.getPhone())
				                         .build();
		// 매퍼 호출
		memberMapper.signUp(signUpMember);
	}

	@Override
	@Transactional 
	public void withdraw(String plainPassword) {
		// 비밀번호 검증 + 유저정보 반환
		CustomUserDetails user = validatePassword(plainPassword);
		
		int tokenResult = tokenMapper.deleteToken(user.getUsername()); // 해당 유저의 토큰 일괄삭제
		if(tokenResult < 1) {
			throw new UnexpectedSQLResponseException("토큰 삭제 실패");
		}
		int memberResult = memberMapper.withdrawByPassword(user.getUsername()); // 멤버의 STATUS 변경
		if(memberResult < 1) {
			throw new UnexpectedSQLResponseException("회원 탈퇴 실패");
		}
	}
	
	private CustomUserDetails validatePassword(String plainPassword) {
		// 사용작 입력한 비밀번호가 DB에 저장된 비밀번호 암호문이랑 맞는지 검증
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		// 검증 실패시 예외 발생
		if(!Password.matches(plainPassword, user.getPassword(), passwordEncoder)) {
			throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
		}
		// 검증 성공시 유저정보 반환
		return user;
	}

}
