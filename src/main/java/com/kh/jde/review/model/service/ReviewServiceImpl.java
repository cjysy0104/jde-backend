package com.kh.jde.review.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewMapper reviewMapper;

	@Override
	public List<ReviewDTO> getReviewList(QueryDTO req, CustomUserDetails principal) {
		
//		req.setMemberNo(Long.valueOf(principal.getMemberNo()));
		req.setMemberNo(Long.valueOf(3));
		
		int size = req.getSize() == null ? 5 : req.getSize();
		req.setSize(size);
		req.setSizePlusOne(size + 1);
		
		// List 조회
		List<ReviewDTO> list = reviewMapper.getReviewList(req);
		
		List<Long> reviewNos = list.stream()
	            .map(ReviewDTO::getReviewNo)
	            .distinct()
	            .toList();
		
		
		return list;
	}

	@Override
	public DetailReviewDTO getDetailReview(Long reviewNo, CustomUserDetails principal) {
		
//		Map<String, Object> param = Map.of(
//			"reviewNo", reviewNo,
//			"memberNo", principal.getMemberNo());
		Map<String, Object> param = Map.of(
				"reviewNo", reviewNo,
				"memberNo", Long.valueOf(3));
		
		return reviewMapper.getDetailReview(param);
	}
}
