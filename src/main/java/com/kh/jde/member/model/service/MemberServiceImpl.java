package com.kh.jde.member.model.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.s3.S3Uploader;
import com.kh.jde.exception.CustomAuthenticationException;
import com.kh.jde.exception.UnexpectedSQLResponseException;
import com.kh.jde.member.model.dao.MemberMapper;
import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.ChangeNameDTO;
import com.kh.jde.member.model.dto.ChangeNicknameDTO;
import com.kh.jde.member.model.dto.ChangePasswordDTO;
import com.kh.jde.member.model.dto.ChangePhoneDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;
import com.kh.jde.member.model.vo.MemberFileVO;
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
	private final S3Uploader s3Uploader;
	
	@Override
	@Transactional
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
		// 회원 가입 SQL문
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
		// 이미 N인 회원은 JwtFilter에서 이미 걸러짐.
		// 입력한 비밀번호는 진짜 비밀번호인지 볼거임.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();

		String encodedPassword = memberMapper.findPasswordByEmail(user.getUsername());
		
		// 비밀번호 검증 실패시 예외 발생
		if(!Password.matches(plainPassword, encodedPassword, passwordEncoder)) {
			throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
		}
		// 검증 성공시 유저정보 반환
		return user;
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordDTO changePassword) {
	    CustomUserDetails user = validatePassword(changePassword.getCurrentPassword());

	    Password newEncoded = Password.toEncoded(changePassword.getNewPassword(), passwordEncoder);

	    MemberVO param = MemberVO.builder()
	            .email(user.getUsername())
	            .password(newEncoded.password())
	            .build();

	    int result = memberMapper.updatePasswordByEmail(param);
	    if (result < 1) {
	        throw new UnexpectedSQLResponseException("비밀번호 변경 실패");
	    }
	}
	
	@Override
	@Transactional
	public void changeName(ChangeNameDTO changeName) {
	    CustomUserDetails user = validatePassword(changeName.getCurrentPassword());

	    MemberVO param = MemberVO.builder()
	            .email(user.getUsername())
	            .memberName(changeName.getMemberName())
	            .build();

	    int result = memberMapper.updateNameByEmail(param);
	    if (result < 1) {
	        throw new UnexpectedSQLResponseException("이름 변경 실패");
	    }
	}
	
	@Override
	@Transactional
	public void changeNickname(ChangeNicknameDTO changeNickname) {
	    CustomUserDetails user = validatePassword(changeNickname.getCurrentPassword());

	    // 닉네임 중복체크 필요하면 여기서 처리 (현재 miv 시그니처에 맞춰 조정)
	    miv.MemberInfomationDuplicateCheck(changeNickname.getNickname(), null, null);

	    MemberVO param = MemberVO.builder()
	            .email(user.getUsername())
	            .nickname(changeNickname.getNickname())
	            .build();

	    int result = memberMapper.updateNicknameByEmail(param);
	    if (result < 1) {
	        throw new UnexpectedSQLResponseException("닉네임 변경 실패");
	    }
	}
	
	@Override
	@Transactional
	public void changePhone(ChangePhoneDTO changePhone) {
	    CustomUserDetails user = validatePassword(changePhone.getCurrentPassword());

	    // 폰 중복체크 필요하면 여기서 처리 (현재 miv 시그니처에 맞춰 조정)
	    miv.MemberInfomationDuplicateCheck(null, null, changePhone.getPhone());

	    MemberVO param = MemberVO.builder()
	            .email(user.getUsername())
	            .phone(changePhone.getPhone())
	            .build();

	    int result = memberMapper.updatePhoneByEmail(param);
	    if (result < 1) {
	        throw new UnexpectedSQLResponseException("전화번호 변경 실패");
	    }
	}

	@Override
	@Transactional
	public String updateMyProfileImage(String plainPassword, MultipartFile file) {
	    // 현재 비밀번호 재검증 + principal 반환
	    CustomUserDetails user = validatePassword(plainPassword);

	    Long memberNo = user.getMemberNo(); // 여기서 바로 사용 (DB 재조회 X)

	    String fileUrl;
	    try {
	        fileUrl = s3Uploader.uploadProfileImage(file, memberNo);
	    } catch (Exception e) {
	        throw new RuntimeException("S3 업로드 실패", e);
	    }

	    MemberFileVO vo = MemberFileVO.builder()
	            .memberNo(memberNo)
	            .fileUrl(fileUrl)
	            .build();

	    int result = memberMapper.upsertProfileImage(vo);
	    if (result < 1) {
	        throw new UnexpectedSQLResponseException("프로필 이미지 저장 실패");
	    }

	    return fileUrl;
	}
	
	@Override // 리뷰로 좋아요 많이 받은 상위3명의 명단 보내기
	@Transactional(readOnly = true)
	public List<CaptainDTO> getCaptainList() {
		
		
		List<CaptainDTO> captains = memberMapper.getCaptainList();
		
		return captains;
	}
	

}

