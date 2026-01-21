package com.kh.jde.review.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateVo {

	private Long reviewNo;
	private String content;
	private float rating;
}
