package com.kh.jde.restaurant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantListDTO {
	
	private Long restaurantNo;
	private String normalName;
	private String address;
	private Float latitude;
	private Float longitude;
	private String thumbnailUrl; // 좋아요 가장 많은 리뷰 썸네일
}
