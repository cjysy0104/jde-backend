package com.kh.jde.member.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.ChangeNameDTO;
import com.kh.jde.member.model.dto.ChangeNicknameDTO;
import com.kh.jde.member.model.dto.ChangePasswordDTO;
import com.kh.jde.member.model.dto.ChangePhoneDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;

public interface MemberService {
	
	void signUp(MemberSignUpDTO member);
	
	void withdraw(String password, CustomUserDetails user);
	
	List<CaptainDTO> getCaptainList();
	
	void changePassword(ChangePasswordDTO changePassword, CustomUserDetails user);
	void changeName(ChangeNameDTO changeName, CustomUserDetails user);
	void changeNickname(ChangeNicknameDTO changeNickname, CustomUserDetails user);
	void changePhone(ChangePhoneDTO changePhone, CustomUserDetails user);
	
	String updateMyProfileImage(String plainPassword, MultipartFile file, CustomUserDetails user);
	
	String changeProfileToDefault(String plainPassword, Long fileNo, CustomUserDetails user);
	
	void verifyPassword(String plainPassword, CustomUserDetails user);
	
}
