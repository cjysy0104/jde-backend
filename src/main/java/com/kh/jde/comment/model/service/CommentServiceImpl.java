package com.kh.jde.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dao.CommentMapper;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentRequestDTO;
import com.kh.jde.comment.model.dto.CommentUpdateDTO;
import com.kh.jde.comment.model.vo.CommentVO;
import com.kh.jde.comment.validator.CommentValidator;
import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.validator.ReviewValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final ReviewMapper reviewMapper;
	private final CommentValidator commentValidator;
	private final ReviewValidator reviewValidator;

	@Override
	public List<CommentDTO> getCommentListById(Long reviewNo) {
		
		// 1. 게시글 상태 조회
		getReviewOrThrow(reviewNo);

		// 2. 댓글 가져와
		List<CommentDTO> comments = commentMapper.getCommentList(reviewNo);
		
		return comments;
	}

	private void getReviewOrThrow(Long reviewNo) {
		reviewValidator.validateReviewExists(reviewMapper.getExistsReview(reviewNo));
	}

	@Override
	@Transactional
	public void create(Long reviewNo, CustomUserDetails principal, CommentRequestDTO request) {

		// 1. 로그인 했지?
		commentValidator.validateAuthenticated(principal);
		
		// 2. 게시글 상태 조회
		getReviewOrThrow(reviewNo);
		
		// 3. 데이터 가공
		CommentVO comment = CommentVO.builder()
				.reviewNo(reviewNo)
				.memberNo(principal.getMemberNo())
				.content(request.getContent())
				.build();
		
		// 4. 댓글 등록해
		commentValidator.validateResult("댓글 등록", commentMapper.create(comment));
	}

	@Override
	@Transactional
	public void deleteById(Long commentNo, CustomUserDetails principal) {
		
		// 1. 댓글 잇냐
		CommentDTO comment = getCommentOrThrow(commentNo);
		
		// 2. 자기꺼임?
		commentValidator.validateOwner(comment, principal);
		
		// 3. 댓글 삭제
		commentValidator.validateResult("댓글 삭제", commentMapper.deleteById(commentNo));
		
	}

	private CommentDTO getCommentOrThrow(Long commentNo) {
		return commentValidator.validateCommentActive(commentMapper.existsComment(commentNo));
	}

	@Override
	@Transactional
	public void update(Long commentNo, CustomUserDetails principal, CommentRequestDTO request) {

		// 1. 댓글 잇냐
		CommentDTO comment = getCommentOrThrow(commentNo);
		
		// 2. 니꺼냐?
		commentValidator.validateOwner(comment, principal);
		
		// 3. 댓글 수정
		CommentUpdateDTO param = CommentUpdateDTO.builder()
												.commentNo(commentNo)
												.content(request.getContent())
												.build();
		
		commentValidator.validateResult("댓글 수정", commentMapper.update(param));
	}
	
}
