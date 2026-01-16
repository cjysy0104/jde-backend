package com.kh.jde.reviewlike.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.reviewlike.vo.ReviewLikeVO;

@Mapper
public interface ReviewLikeMapper {

	int existsReview(Long reviewNo);          // 리뷰 존재(정상) 여부
	
    int existsLike(ReviewLikeVO reviewLike);  // 좋아요 존재 여부

    int insertLike(ReviewLikeVO reviewLike);  // 좋아요 등록

    int deleteLike(ReviewLikeVO reviewLike);  // 좋아요 취소(삭제)

    int countLikes(Long reviewNo);            // 좋아요 수
    
}
