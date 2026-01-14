package com.kh.jde.report.model.vo;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class ReportCategoryVO {
	
	private Long reportCategoryNo;
	private String reportCategoryTitle;
}
