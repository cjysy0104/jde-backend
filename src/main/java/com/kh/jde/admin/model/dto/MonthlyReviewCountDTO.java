package com.kh.jde.admin.model.dto;

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
public class MonthlyReviewCountDTO {
	
	private String yearMonth; // YYYY-MM 형식 (예: "2024-01")
	private int count; // 해당 월의 리뷰 수
}
