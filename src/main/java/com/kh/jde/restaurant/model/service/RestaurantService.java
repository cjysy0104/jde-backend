package com.kh.jde.restaurant.model.service;

import java.util.List;

import com.kh.jde.restaurant.model.dto.RestaurantListDTO;
import com.kh.jde.restaurant.model.dto.RestaurantQueryDTO;
import com.kh.jde.restaurant.model.dto.RestaurantSearchDTO;

public interface RestaurantService {

	// 레스토랑 무한 스크롤 조회 (전체 목록)
	List<RestaurantListDTO> getRestaurantList(RestaurantQueryDTO request);

	// 레스토랑 키워드 검색 무한 스크롤 조회
	List<RestaurantListDTO> searchRestaurant(RestaurantSearchDTO request);
}
