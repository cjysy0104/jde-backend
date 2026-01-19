package com.kh.jde.review.model.dto;

import java.sql.Date;
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
public class ReviewListResponseDTO {

	private Long reviewNo;
	
	private String thumbnailUrl;
	private String restaurantName;
	private String nickname;
	
	private String content;
	private float rating;
	private Date updateDate;
	
	private int viewCount;
	private int likeCount;
	private int commentCount;
	
	private String isLiked;
	private String isMarked;
	
	private List<KeywordDTO> keywords;
}
