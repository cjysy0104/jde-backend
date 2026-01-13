package com.kh.jde.member.model.service;

import com.kh.jde.member.model.dto.MemberDTO;
import com.kh.jde.member.model.dto.MemberSignUpDTO;

public interface MemberService {
	
	void signUp(MemberSignUpDTO member);
	
	void withdraw(String password);

}
