package com.kh.jde.review.model.dto;

import java.util.List;

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
public class BestReviewListResponse {

	private Long reviewNo;
	
	private String thumbnailUrl;
	private String restaurantName;
	private String nickname;
	
	private int recentLikeCount;
	
	private float rating;
	
	private List<KeywordDTO> keywords;
}
