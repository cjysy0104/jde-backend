package com.kh.jde.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaptainQueryDTO {
	
	// 미식대장 번호
	private Long captainNo;
	
	// 리뷰 전체 조회시 사용하는 QueryDTO
	private QueryDTO query;

}
