package com.kh.jde.restaurant.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.restaurant.model.dto.RestaurantListDTO;
import com.kh.jde.restaurant.model.dto.RestaurantQueryDTO;
import com.kh.jde.restaurant.model.dto.RestaurantSearchDTO;

@Mapper
public interface RestaurantMapper {

	// 레스토랑 무한 스크롤 조회 (전체 목록)
	List<RestaurantListDTO> selectRestaurantList(RestaurantQueryDTO request);

	// 레스토랑 키워드 검색 무한 스크롤 조회
	List<RestaurantListDTO> selectRestaurantListByKeyword(RestaurantSearchDTO request);
}
