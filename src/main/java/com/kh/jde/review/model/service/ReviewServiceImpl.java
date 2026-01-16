package com.kh.jde.review.model.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.KeywordDTO;
import com.kh.jde.review.model.dto.KeywordRowDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewMapper reviewMapper;

	@Override
	public List<ReviewDTO> getReviewList(QueryDTO req, CustomUserDetails principal) {
		
		// 쿼리DTO 가공
		QueryDTO normalized = nomarizedRequest(req, principal);
		
		// 목록 조회
		List<ReviewDTO> reviews = reviewMapper.getReviewList(normalized);
		
		// 조회한애들 No 뽑아서 키워드 조회하고 주입해
		attachKeyword(reviews);

		return reviews;
	}

	private void attachKeyword(List<ReviewDTO> reviews) {
		
		// 0. reviewNo빼내서 리스트화
		List<Long> reviewNos = reviews.stream()
        .map(ReviewDTO::getReviewNo)
        .distinct()
        .toList();
		
		// 1. 조회해와
		List<KeywordRowDTO> keywordsRow = reviewMapper.getKeywordsByIds(reviewNos);
		
		// 2. reviews에 담을 수 있도록 Map<Long(No), List<keywordDTO>> 생성 및 그룹화 완료
		Map<Long, List<KeywordDTO>> keywordMap = keywordsRow.stream()
				.collect(Collectors.groupingBy(row -> row.getReviewNo(), Collectors.mapping(
						row -> new KeywordDTO(row.getKeywordNo(), row.getKeywordName()), 
						Collectors.toList())));
		
		// 3. 주입
		for (ReviewDTO review : reviews) {
		    review.setKeywords(
		        keywordMap.getOrDefault(review.getReviewNo(), List.of()));
		}

	}

	
	private QueryDTO nomarizedRequest(QueryDTO req, CustomUserDetails principal) {

		// 0. query null 방지
		if(req == null) req = new QueryDTO();
		
		// 1. 로그인? 했으면 memberNo 주입 / 아니면 null
		if(principal != null) {
			req.setMemberNo(principal.getMemberNo());
		} else {
//			req.setMemberNo(null);
			req.setMemberNo(Long.valueOf(3)); // test용
		}
		
		// 2. scroll 만들자
		int size = req.getSize() == null ? 5 : req.getSize();
		req.setSize(size);
		req.setSizePlusOne(size + 1);
		
		return req;
	}

	@Override
	public DetailReviewDTO getDetailReview(Long reviewNo, CustomUserDetails principal) {
		
		Map<String, Object> param = Map.of(
				"reviewNo", reviewNo,
				"memberNo", Long.valueOf(3));
		
		return reviewMapper.getDetailReview(param);
	}
}
