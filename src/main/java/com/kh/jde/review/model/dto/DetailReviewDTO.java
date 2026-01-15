package com.kh.jde.review.model.dto;

import java.sql.Date;
import java.util.List;

import com.kh.jde.member.model.dto.CaptainDTO;

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
public class DetailReviewDTO {

	// 게시글 정보
	private Long reviewNo;
	private String content;
	private float rating;
	private Date createDate;
	private Date updateDate;
	private int viewCount;
	private int likeCount;
	private String isLiked;
	private String isMarked;
	
	// 작성자
	private CaptainDTO writer;
	
	// 식당 정보
	private RestaurantDTO restaurant;
	
	// 이미지
	private List<ReviewFileDTO> files;
	
	// 키워드
	private List<KeywordDTO> keywords;
}
