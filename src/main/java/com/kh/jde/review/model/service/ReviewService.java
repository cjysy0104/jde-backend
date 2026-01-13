package com.kh.jde.review.model.service;

import java.util.List;

import com.kh.jde.review.model.dto.ReviewDTO;

public interface ReviewService {

	public List<ReviewDTO> findAll();
}
