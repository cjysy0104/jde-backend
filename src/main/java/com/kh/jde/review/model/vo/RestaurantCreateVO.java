package com.kh.jde.review.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreateVO {

	private Long restaurantNo;
	private String normalName;
	private String address;
	private float latitude;
	private float longitude;
}
