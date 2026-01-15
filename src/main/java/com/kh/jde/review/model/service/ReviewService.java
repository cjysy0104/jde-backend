package com.kh.jde.review.model.service;

import java.util.List;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewDTO;

public interface ReviewService {

	public List<ReviewDTO> findAll(QueryDTO req, CustomUserDetails principal);
	
	public DetailReviewDTO findById(Long reviewNo, CustomUserDetails principal);
}
