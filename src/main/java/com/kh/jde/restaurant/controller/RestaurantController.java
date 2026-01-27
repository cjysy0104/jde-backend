package com.kh.jde.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.restaurant.model.dto.RestaurantListDTO;
import com.kh.jde.restaurant.model.dto.RestaurantQueryDTO;
import com.kh.jde.restaurant.model.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	
	// 레스토랑 무한 스크롤 조회
	@GetMapping
	public ResponseEntity<SuccessResponse<List<RestaurantListDTO>>> getRestaurantList(
			@ModelAttribute RestaurantQueryDTO request) {
		
		List<RestaurantListDTO> restaurantList = restaurantService.getRestaurantList(request);
		
		return SuccessResponse.ok(restaurantList, "레스토랑 목록 조회 성공");
	}
}
