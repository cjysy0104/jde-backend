package com.kh.jde.review.model.dto;

import java.util.List;

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
public class ScrollRequest {
	//스크롤
	private Integer size;
	private Integer sizePlusOne; // size + 1
}
