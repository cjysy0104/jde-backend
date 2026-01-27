package com.kh.jde.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dao.CommentMapper;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentRequestDTO;
import com.kh.jde.comment.model.dto.CommentResponse;
import com.kh.jde.comment.model.dto.CommentUpdateDTO;
import com.kh.jde.comment.model.vo.CommentVO;
import com.kh.jde.comment.validator.CommentValidator;
import com.kh.jde.common.page.PageInfo;
import com.kh.jde.common.page.Pagination;
import com.kh.jde.common.util.RequestNormalizer;
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
	private final RequestNormalizer requestNormalizer;

	@Override
	public CommentResponse getCommentListById(Long reviewNo, int page, CustomUserDetails principal) {
		
		// 1. 게시글 상태 조회
		getReviewOrThrow(reviewNo);
		
		// 2. 댓글 개수 조회
		int listCount = commentMapper.getCommentCount(reviewNo);
		
		// 3. pageInfo 계산
		int pageLimit = 5;
		int boardLimit = 10;

		PageInfo pi = Pagination.getPageInfo(listCount, page, pageLimit, boardLimit);
		
		// 4. 댓글 조회
		List<CommentDTO> comments = commentMapper.getCommentList(reviewNo, pi);
		
		Long memberNo = (principal != null) ? principal.getMemberNo() : null;
		
		// 5. 댓글 본인 여부 추가
		for(CommentDTO comment : comments) {
			if(memberNo != null && memberNo.equals(comment.getMemberNo())) {
				comment.setIsOwner("Y");
			} else {
				comment.setIsOwner("N");
			}
		}
		
		return new CommentResponse(comments, pi);
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
	
	@Override
	public List<CommentDTO> getMyComments(com.kh.jde.review.model.dto.QueryDTO req, CustomUserDetails principal) {
	    // 1. 인증 검증
	    commentValidator.validateAuthenticated(principal);

	    // 2. req가 null인 경우 초기화
	    if (req == null) req = new com.kh.jde.review.model.dto.QueryDTO();
	    // 3. 페이지(cursor) 및 사이즈 기본값 설정
	    // offset 계산 시 null 에러 방지를 위해 1페이지(1L), 10개(10)를 기본으로 함
	    if (req.getCursor() == null || req.getCursor() <= 0) req.setCursor(1L); 
	    req.setScroll(requestNormalizer.applyScroll(req.getScroll(), 10));
	    // 4. 내 memberNo 세팅
	    req.setMemberNo(principal.getMemberNo());

	    // 5. 조회 실행
	    return commentMapper.getMyComments(req);
	}

}
