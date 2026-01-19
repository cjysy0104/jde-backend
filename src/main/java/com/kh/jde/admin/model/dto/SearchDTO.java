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
public class SearchDTO {
	
	private String keyword; // 검색 키워드
	private int currentPage = 1; // 현재 페이지 (기본값 1)
}
