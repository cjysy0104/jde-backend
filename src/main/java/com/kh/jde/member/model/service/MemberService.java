package com.kh.jde.member.model.service;

import java.util.List;

import com.kh.jde.member.model.dto.CaptainDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;

public interface MemberService {
	
	void signUp(MemberSignUpDTO member);
	
	void withdraw(String password);
	
	List<CaptainDTO> getCaptainList();

}
