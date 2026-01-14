package com.kh.jde.report.model.dto;

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
public class ReviewReportListDTO {
	
	private Long reportNo;
	private Long reviewReportNo;
	private Long memberNo;
	private String memberNickname; // 신고한 회원 닉네임
	private Long reviewNo;
	private String reviewContent; // 신고된 리뷰 내용
	private Long reportCategoryNo;
	private String reportCategoryTitle; // 신고 카테고리명
	private String reportContent;
	private String reportProcess;
	private Date createdAt;
	private Date updateAt;
	private String status;
}
