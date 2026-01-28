package com.kh.jde.restaurant.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.common.util.RequestNormalizer;
import com.kh.jde.restaurant.model.dao.RestaurantMapper;
import com.kh.jde.restaurant.model.dto.RestaurantListDTO;
import com.kh.jde.restaurant.model.dto.RestaurantQueryDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
	
	private final RestaurantMapper restaurantMapper;
	private final RequestNormalizer requestNormalizer;
	private static final int DEFAULT_SIZE = 20; // 기본 조회 개수
	
	@Override
	@Transactional(readOnly = true)
	public List<RestaurantListDTO> getRestaurantList(RestaurantQueryDTO request) {
		// 쿼리DTO 가공
		RestaurantQueryDTO normalized = normalizeRequest(request);
		
		// 목록 조회
		List<RestaurantListDTO> restaurants = restaurantMapper.selectRestaurantList(normalized);
		return restaurants;
	}
	
	private RestaurantQueryDTO normalizeRequest(RestaurantQueryDTO request) {
		// 0. request null 방지
		if (request == null) request = new RestaurantQueryDTO();
		
		// 1. scroll 만들기
		request.setScroll(requestNormalizer.applyScroll(request.getScroll(), DEFAULT_SIZE));
		
		return request;
	}
}
