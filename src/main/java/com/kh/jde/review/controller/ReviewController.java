package com.kh.jde.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewCreateRequest;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;
import com.kh.jde.review.model.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	
	@GetMapping
	public ResponseEntity<SuccessResponse<List<ReviewListResponseDTO>>> getReviewList(@ModelAttribute QueryDTO req
																, @AuthenticationPrincipal CustomUserDetails principal){
		
		List<ReviewListResponseDTO> result = reviewService.getReviewList(req, principal);
		
		return SuccessResponse.ok(result, "리뷰 전체 조회 성공");
	}
	
	@GetMapping("/{reviewNo}")
	public ResponseEntity<SuccessResponse<ReviewListResponseDTO>> getDetailReview(@PathVariable("reviewNo") Long reviewNo
															, @AuthenticationPrincipal CustomUserDetails principal){
		
		return SuccessResponse.ok(reviewService.getDetailReview(reviewNo, principal), "리뷰 상세 조회 성공");
	}
	
	
	@DeleteMapping("/{reviewNo}")
	public ResponseEntity<SuccessResponse<Void>> deleteById(@PathVariable("reviewNo")Long reviewNo
															, @AuthenticationPrincipal CustomUserDetails principal){
		reviewService.deleteById(reviewNo, principal);
		
		return SuccessResponse.ok("삭제 성공");
	} 
	
	@PostMapping
	public ResponseEntity<SuccessResponse<Void>> create(@ModelAttribute @Valid ReviewCreateRequest review
			, @AuthenticationPrincipal CustomUserDetails principal){
		
		reviewService.create(review, principal);
		
		return SuccessResponse.created("등록 성공");
	}

	@GetMapping("/me")
	public ResponseEntity<SuccessResponse<List<ReviewListResponseDTO>>> getMyReviews(
	        @ModelAttribute QueryDTO req,
	        @AuthenticationPrincipal CustomUserDetails principal
	) {
		// 로그인 필수
	    if (principal == null) throw new com.kh.jde.exception.AccessDeniedException("로그인이 필요합니다.");

	    List<ReviewListResponseDTO> result = reviewService.getMyReviewList(req, principal);
	    return SuccessResponse.ok(result, "내 리뷰 조회 성공");
	}

}
