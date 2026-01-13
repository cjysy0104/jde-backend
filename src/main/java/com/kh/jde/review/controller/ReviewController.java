package com.kh.jde.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.review.model.dto.ReviewDTO;
import com.kh.jde.review.model.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	
	@GetMapping
	public ResponseEntity<SuccessResponse<List<ReviewDTO>>> findAll(){
		
		List<ReviewDTO> result = reviewService.findAll();
		
		return SuccessResponse.ok(result, "리뷰 전체 조회 성공");
	}
	
}
