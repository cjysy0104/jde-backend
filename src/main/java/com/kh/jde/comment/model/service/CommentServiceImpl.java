package com.kh.jde.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dao.CommentMapper;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.vo.CommentVO;
import com.kh.jde.exception.PostNotFoundException;
import com.kh.jde.exception.UnexpectedSQLResponseException;
import com.kh.jde.review.model.dao.ReviewMapper;

import jakarta.transaction.Transactional;
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
			throw new PostNotFoundException("조회된 게시글이 없습니다.");
		}
		
		// 2. 댓글 가져와
		List<CommentDTO> comments = commentMapper.getCommentList(reviewNo);
		
		return comments;
	}

	@Override
	@Transactional
	public int create(Long reviewNo, CustomUserDetails principal, CommentDTO request) {

		// 1. 게시글 상태 조회
		if(reviewMapper.existsReview(reviewNo) == 0) {
			throw new PostNotFoundException("조회된 게시글이 없습니다.");
		}
		
		// 2. 데이터 가공
		CommentVO comment = CommentVO.builder()
				.reviewNo(reviewNo)
				.memberNo(principal.getMemberNo())
				.content(request.getContent())
				.build();
		
		// 3. 댓글 등록해
		int result = commentMapper.create(comment);
		
		if(result == 0) {
			throw new UnexpectedSQLResponseException("댓글 등록에 실패했습니다.");
		}
		return result; 
	}

	@Override
	public int deleteById(Long commentNo) {
		
		// 1. 댓글 잇냐
		if(commentMapper.existsComment(commentNo) == null) {
			throw new PostNotFoundException("댓글이 존재하지 않습니다.");
		}
		// 2. 댓글 삭제
		int result = commentMapper.deleteById(commentNo);
		
		return result;
	}
	
	
	
}
