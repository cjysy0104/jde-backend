package com.kh.jde.review.validator;

import org.springframework.stereotype.Component;

import com.kh.jde.common.constants.Status;
import com.kh.jde.exception.AccessDeniedException;
import com.kh.jde.exception.AlreadyDeletedException;
import com.kh.jde.exception.PostNotFoundException;
import com.kh.jde.exception.UnexpectedSQLResponseException;
import com.kh.jde.exception.UsernameNotFoundException;
import com.kh.jde.review.model.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

	// 게시글 존재 여부
	public ReviewDTO validateReviewExists(ReviewDTO review) {
		
		if(review == null) {
			throw new PostNotFoundException("조회된 게시글이 없습니다.");
		}
		
		if(Status.DELETED.equals(review.getStatus())) {
			throw new AlreadyDeletedException("이미 삭제된 게시물입니다.");
		}
		
		return review;
		
	}
	
	// 게시글 작성자 비교기
	public Long validateWriter(Long writerNo, Long userNo, String action) {
		if(!(writerNo.equals(userNo))) {
			throw new AccessDeniedException(action + " 권한이 없습니다.");
		}
		
		if(writerNo.equals(null)) {
			throw new UsernameNotFoundException("작성자를 찾을 수 없습니다.");
		}
		
		return writerNo;
	}
	
	// sql 성공?
	public void validateResult(int result) {
		
		if(result != 1) {
			throw new UnexpectedSQLResponseException("SQL 오류가 발생했습니다.");
		}
	}
}
