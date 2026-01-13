package com.kh.jde.review.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QueryDTO {

	private String query;
	private String keyword;
	private Float minRating = (float) 0;
	private Float maxRating = (float) 5;
	
	private Integer page;
	private Integer size;
	private String sort;
	
}
