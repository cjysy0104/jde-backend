package com.kh.jde.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.jde.comment.model.dao.CommentMapper;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.exception.PostNotFoundException;
import com.kh.jde.review.model.dao.ReviewMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final ReviewMapper reviewMapper;

	@Override
	public List<CommentDTO> getCommentListById(Long reviewNo) {
		
		// 1. 게시글 상태 조회
		if(reviewMapper.existsReview(reviewNo) == 0) {
			log.info("NO???{}", reviewMapper.existsReview(reviewNo));
			throw new PostNotFoundException("조회된 게시글이 없습니다.");
		}
		
		// 2. 댓글 가져와
		List<CommentDTO> comments = commentMapper.getCommentList(reviewNo);
		
		return comments;
	}
	
	
	
}
