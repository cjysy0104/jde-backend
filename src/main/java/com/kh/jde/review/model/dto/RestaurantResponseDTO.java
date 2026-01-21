package com.kh.jde.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantResponseDTO {

	private Long restaurantNo;
	private String normalName;
	private String address;
	private float latitude;
	private float longitude;
}
