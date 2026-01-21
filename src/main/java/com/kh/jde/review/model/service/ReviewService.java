package com.kh.jde.review.model.service;

import java.util.List;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.review.model.dto.BestReviewListResponse;
import com.kh.jde.review.model.dto.BestReviewPagingRequest;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewCreateRequest;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;
import com.kh.jde.review.model.dto.ReviewUpdateRequest;

import jakarta.validation.Valid;

public interface ReviewService {

	public List<ReviewListResponseDTO> getReviewList(QueryDTO req, CustomUserDetails principal);
	
	public DetailReviewDTO getDetailReview(Long reviewNo, CustomUserDetails principal);

	public void deleteById(Long reviewNo, CustomUserDetails principal);

	public void create(ReviewCreateRequest review, CustomUserDetails principal);
	
	List<ReviewListResponseDTO> getMyReviewList(QueryDTO req, CustomUserDetails principal);

	public void update(Long reviewNo, ReviewUpdateRequest review, CustomUserDetails principal);

	public List<BestReviewListResponse> getBestReviewList(BestReviewPagingRequest req);

}
