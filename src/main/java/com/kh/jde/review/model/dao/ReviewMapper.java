package com.kh.jde.review.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.jde.review.model.dto.BestReviewListResponse;
import com.kh.jde.review.model.dto.BestReviewPagingRequest;
import com.kh.jde.review.model.dto.CaptainQueryDTO;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.KeywordRowDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.RestaurantRequestDTO;
import com.kh.jde.review.model.dto.RestaurantResponseDTO;
import com.kh.jde.review.model.dto.ReviewDTO;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;
import com.kh.jde.review.model.vo.RestaurantCreateVO;
import com.kh.jde.review.model.vo.ReviewCreateVO;
import com.kh.jde.review.model.vo.ReviewFileCreateVO;
import com.kh.jde.review.model.vo.ReviewUpdateVo;

@Mapper
public interface ReviewMapper {

	// 전체 목록 조회
	List<ReviewListResponseDTO> getReviewList(QueryDTO req);
	
	// 미식대장의 리뷰 전체 조회
	List<ReviewListResponseDTO> getCaptainReviewList(CaptainQueryDTO req);
	
	// 리뷰 상세 조회
	DetailReviewDTO getDetailReview(Map<String,Object> param);
	
	List<KeywordRowDTO> getKeywordsByIds(List<Long> reviewNo);
	
	Long getWriterById(Long reviewNo);
	
	// 리뷰 작성
	void createReview(ReviewCreateVO requestReview);
	
	void createReviewKeywordMap(@Param("reviewNo") Long reviewNo,
			@Param("keywordNos") List<Long> keywordNos);
	
	void createRestaurant(RestaurantCreateVO createRestaurant);
	
	void createReviewFile(ReviewFileCreateVO reviewFileCreateVO);
	
	// 리뷰 수정
	void update(ReviewUpdateVo requestReview);
	
	// 리뷰 삭제(소프트)
	int deleteById(Long reviewNo);
	
	int existsReview(Long reviewNo);	// 리뷰 존재(정상) 여부

	ReviewDTO getExistsReview(Long reviewNo);
	
	RestaurantResponseDTO getRestaurantByName(RestaurantRequestDTO request);
	

	List<ReviewListResponseDTO> getMyReviewList(QueryDTO req);

	void deleteKeywordsById(Long reviewNo);

	void deleteFilesById(Long reviewNo);

	List<String> getUrlById(Long reviewNo);

	List<BestReviewListResponse> getBestReviewList(BestReviewPagingRequest req);

}
