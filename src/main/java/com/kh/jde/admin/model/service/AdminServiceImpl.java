package com.kh.jde.admin.model.service;

import org.springframework.stereotype.Service;

import com.kh.jde.admin.model.dao.AdminMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final AdminMapper adminMapper;
	

}
