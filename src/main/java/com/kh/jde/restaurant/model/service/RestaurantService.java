package com.kh.jde.restaurant.model.service;

import java.util.List;

import com.kh.jde.restaurant.model.dto.RestaurantListDTO;
import com.kh.jde.restaurant.model.dto.RestaurantQueryDTO;

public interface RestaurantService {
	
	// 레스토랑 무한 스크롤 조회
	List<RestaurantListDTO> getRestaurantList(RestaurantQueryDTO request);
}
