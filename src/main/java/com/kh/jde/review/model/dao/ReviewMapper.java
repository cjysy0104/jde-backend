package com.kh.jde.review.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.KeywordRowDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewDTO;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;

@Mapper
public interface ReviewMapper {

	// 전체 목록 조회
	List<ReviewListResponseDTO> getReviewList(QueryDTO req);
	
	// 리뷰 상세 조회
	DetailReviewDTO getDetailReview(Map<String,Object> param);
	
	List<KeywordRowDTO> getKeywordsByIds(List<Long> reviewNo);
	
	Long getWriterById(Long reviewNo);
	
	// 리뷰 작성
	void save();
	
	// 리뷰 수정
	void update();
	
	// 리뷰 삭제(소프트)
	int deleteById(Long reviewNo);
	
	int existsReview(Long reviewNo);	// 리뷰 존재(정상) 여부

	ReviewDTO getExistsReview(Long reviewNo);
}
