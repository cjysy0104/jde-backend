package com.kh.jde.review.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFileCreateVO {

	private Long reviewNo;
	private String fileUrl;
	private int sortOrder;
	private String isThumbnail;
}
