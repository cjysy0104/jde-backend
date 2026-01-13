package com.kh.jde.commentLike.model.service;

import org.springframework.stereotype.Service;

import com.kh.jde.commentLike.model.dao.CommentLikeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
	
	private final CommentLikeMapper commentLikeMapper;
	
}
