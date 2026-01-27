package com.kh.jde.restaurant.model.dto;

import com.kh.jde.review.model.dto.ScrollRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantQueryDTO {
	
	// 스크롤
	private ScrollRequest scroll;
	
	// 커서 (레스토랑 번호)
	private Long cursor;
}
