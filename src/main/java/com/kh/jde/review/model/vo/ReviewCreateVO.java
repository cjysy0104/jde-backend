package com.kh.jde.review.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateVO {

	private Long reviewNo;
	private Long memberNo;
	private Long restaurantNo;
	private String content;
	private float rating;
	private String address;
	
}
