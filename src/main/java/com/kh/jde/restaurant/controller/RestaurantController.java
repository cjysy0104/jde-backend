package com.kh.jde.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.restaurant.model.dto.RestaurantListDTO;
import com.kh.jde.restaurant.model.dto.RestaurantQueryDTO;
import com.kh.jde.restaurant.model.dto.RestaurantSearchDTO;
import com.kh.jde.restaurant.model.service.RestaurantService;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;
import com.kh.jde.review.model.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	private final ReviewService reviewService;
	
	// 레스토랑 무한 스크롤 조회 (전체 목록)
	@GetMapping
	public ResponseEntity<SuccessResponse<List<RestaurantListDTO>>> getRestaurantList(
			@ModelAttribute RestaurantQueryDTO request) {

		List<RestaurantListDTO> restaurantList = restaurantService.getRestaurantList(request);

		return SuccessResponse.ok(restaurantList, "레스토랑 목록 조회 성공");
	}

	// 레스토랑 키워드 검색
	@GetMapping("/search")
	public ResponseEntity<SuccessResponse<List<RestaurantListDTO>>> searchRestaurant(
			@ModelAttribute RestaurantSearchDTO request) {

		List<RestaurantListDTO> restaurantList = restaurantService.searchRestaurant(request);

		return SuccessResponse.ok(restaurantList, "레스토랑 검색 성공");
	}

	// 레스토랑별 리뷰 조회
	@GetMapping("/{restaurantNo}/reviews")
	public ResponseEntity<SuccessResponse<List<ReviewListResponseDTO>>> getRestaurantReviews(
			@PathVariable("restaurantNo") Long restaurantNo,
			@ModelAttribute QueryDTO request,
			@AuthenticationPrincipal CustomUserDetails principal) {
		
		// 레스토랑 번호 설정
		if (request == null) {
			request = new QueryDTO();
		}
		request.setRestaurantNo(restaurantNo);
		
		List<ReviewListResponseDTO> reviewList = reviewService.getReviewList(request, principal);
		
		return SuccessResponse.ok(reviewList, "레스토랑 리뷰 조회 성공");
	}

}
