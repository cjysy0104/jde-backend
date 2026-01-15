package com.kh.jde.review.model.dto;


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
public class RestaurantDTO {

	private Long restaurantNo;
	private String normalName;
	private String address;
	private float latitude;
	private float longitude;
}
