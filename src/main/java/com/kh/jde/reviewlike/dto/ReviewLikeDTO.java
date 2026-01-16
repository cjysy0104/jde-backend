package com.kh.jde.reviewlike.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewLikeDTO {
	
	private Long reviewNo;
    private Boolean isLiked;
    private Integer likeCount;
	
}
