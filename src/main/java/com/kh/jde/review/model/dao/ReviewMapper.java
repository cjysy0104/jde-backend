package com.kh.jde.review.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewDTO;

@Mapper
public interface ReviewMapper {

	// 전체 목록 조회
	List<ReviewDTO> findAll(QueryDTO req);
	
	// 리뷰 상세 조회
	ReviewDTO findById();
	
	// 리뷰 작성
	void save();
	
	// 리뷰 수정
	void update();
	
	// 리뷰 삭제(소프트)
	void deleteById();
}
