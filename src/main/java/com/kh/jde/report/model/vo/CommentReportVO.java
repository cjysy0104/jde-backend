package com.kh.jde.report.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class CommentReportVO {
	
	private Long commentReportNo;
	private Long memberNo;
	private Long commentNo;
	private Long reportCategoryNo;
	private String reportContent;
	private String reportProcess; // PENDING, IN_PROGRESS, RESOLVED, REJECTED
	private Date createdAt;
	private Date updateAt;
	private String status; // Y, N
}
