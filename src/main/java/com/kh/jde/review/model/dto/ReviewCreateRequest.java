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
public class ReviewCreateRequest {

	// review
	private String content;
	private float rating;
	private List<Long> keywordNos;
	
	// restaurant address
	private String normalName;
	private String address;
	private float latitude;
	private float Longitude;
	
	// Files
	private List<MultipartFile> images;
	
}
