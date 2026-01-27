package com.kh.jde.review.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QueryDTO {

	// 검색 / 필터
	private String query;
	private String keyword;
	private Float minRating = (float) 0;
	private Float maxRating = (float) 5;
	private Long restaurantNo; // 레스토랑 번호 필터
	
	// 로그인 사용자
	private Long memberNo;
	
	// 스크롤
	private ScrollRequest scroll;
	private String sort;	// latest | oldest | rating | liked
	
	// 커서
	private Long cursor;
	
	private Float cursorRating; // sort = rating일 때,
	private Integer cursorLikedCount; // sort = liked일 때 혹은 다른 정렬기능 추가 시
	
	
}
