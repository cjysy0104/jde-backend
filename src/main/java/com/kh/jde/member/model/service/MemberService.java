package com.kh.jde.member.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.ChangeNameDTO;
import com.kh.jde.member.model.dto.ChangeNicknameDTO;
import com.kh.jde.member.model.dto.ChangePasswordDTO;
import com.kh.jde.member.model.dto.ChangePhoneDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;

public interface MemberService {
	
	void signUp(MemberSignUpDTO member);
	
	void withdraw(String password);
	
	List<CaptainDTO> getCaptainList();
	
	void changePassword(ChangePasswordDTO changePassword);
	void changeName(ChangeNameDTO changeName);
	void changeNickname(ChangeNicknameDTO changeNickname);
	void changePhone(ChangePhoneDTO changePhone);
	
	String updateMyProfileImage(String plainPassword, MultipartFile file);
}
