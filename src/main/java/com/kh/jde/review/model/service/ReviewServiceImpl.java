package com.kh.jde.review.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewMapper reviewMapper;

	@Override
	public List<ReviewDTO> findAll(QueryDTO req) {
		
		return reviewMapper.findAll(req);
	}
}
