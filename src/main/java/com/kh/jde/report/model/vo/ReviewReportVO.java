package com.kh.jde.report.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class ReviewReportVO {
	
	private Long reviewReportNo;
	private Long memberNo;
	private Long reviewNo;
	private Long reportCategoryNo;
	private String reportContent;
	private String reportProcess; // PENDING, IN_PROGRESS, RESOLVED, REJECTED
	private Date createdAt;
	private Date updateAt;
	private String status; // Y, N
}
