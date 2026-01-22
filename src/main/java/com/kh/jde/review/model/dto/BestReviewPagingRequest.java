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
public class BestReviewPagingRequest {

	private ScrollRequest scroll;
	
	// 커서
	private Long cursor;
	private Integer cursorLikeCount;
}
