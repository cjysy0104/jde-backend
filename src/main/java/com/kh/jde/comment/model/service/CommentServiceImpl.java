package com.kh.jde.comment.model.service;

import org.springframework.stereotype.Service;

import com.kh.jde.comment.model.dao.CommentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	
	
	
}
