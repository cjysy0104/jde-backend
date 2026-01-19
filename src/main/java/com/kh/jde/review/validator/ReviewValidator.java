package com.kh.jde.review.validator;

import org.springframework.stereotype.Component;

import com.kh.jde.common.constants.Status;
import com.kh.jde.exception.AlreadyDeletedException;
import com.kh.jde.exception.PostNotFoundException;
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
}
