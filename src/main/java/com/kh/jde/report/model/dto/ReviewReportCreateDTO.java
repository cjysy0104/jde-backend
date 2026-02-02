package com.kh.jde.report.model.dto;

import jakarta.validation.constraints.NotNull;
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
public class ReviewReportCreateDTO {
	
	private Long memberNo; // Service에서 설정
	
	@NotNull(message = "리뷰 번호는 필수입니다.")
	private Long reviewNo;
	
	@NotNull(message = "신고 카테고리 번호는 필수입니다.")
	private Long reportCategoryNo;
	
	private String reportContent; // 선택사항
}
