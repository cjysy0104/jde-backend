package com.kh.jde.review.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

	private Long reviewNo;
	private Long memberNo;
	private Long restaurantNo;
	private String content;
	private float rating;
	private String status;
	private Date createdAt;
	private Date updatedAt;
	private int viewCount;
}
