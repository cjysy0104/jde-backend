package com.kh.jde.auth.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.exception.AccessDeniedException;
import com.kh.jde.member.model.dao.MemberMapper;
import com.kh.jde.member.model.dto.MemberLoginDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberMapper mapper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberLoginDTO user = mapper.loadUser(username);
		// log.info("유저정보 조회결과 : {}", user);
		if(user == null) {
			throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
		} else if ("N".equals(user.getStatus())) {
		    throw new AccessDeniedException("정지된 계정입니다.");
		}
		return CustomUserDetails.builder().username(user.getEmail())
				  						  .password(user.getPassword())
				  						  .memberName(user.getMemberName())
				  						  .memberNo(user.getMemberNo())
				  						  .phone(user.getPhone())
				  						  .nickname(user.getNickname())
				  						  .email(user.getEmail())
				  						  .enrollDate(user.getEnrollDate())
				  						  .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
				  						  .status(user.getStatus())
				  						  .role(user.getRole())
				  						  .build();
	}

}
