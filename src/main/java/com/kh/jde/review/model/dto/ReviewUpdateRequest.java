package com.kh.jde.review.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewUpdateRequest {

	// review
	private String content;
	private float rating;
	private List<Long> keywordNos;
	
	// Files
	private List<MultipartFile> images;
}
