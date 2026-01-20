package com.kh.jde.admin.model.dto;

import java.sql.Date;

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
public class ReviewListDTO {
	
	private Long reviewNo; // 리뷰 번호
	private Long memberNo; // 신고자 번호
	private String restaurantNo; // 식당 번호
	private String normalName; // 식당 이름
	private String address; // 식당 주소
	private String reviewContent; // 리뷰 내용
	private Date createdAt; // 리뷰 작성일
	private String status;
}
