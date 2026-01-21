package com.kh.jde.review.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.exception.AccessDeniedException;
import com.kh.jde.exception.PostNotFoundException;
import com.kh.jde.file.service.S3Service;
import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.KeywordDTO;
import com.kh.jde.review.model.dto.KeywordRowDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.RestaurantRequestDTO;
import com.kh.jde.review.model.dto.RestaurantResponseDTO;
import com.kh.jde.review.model.dto.ReviewCreateRequest;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;
import com.kh.jde.review.model.vo.RestaurantCreateVO;
import com.kh.jde.review.model.vo.ReviewCreateVO;
import com.kh.jde.review.model.vo.ReviewFileCreateVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewMapper reviewMapper;
	private final S3Service s3service;

	@Override
	public List<ReviewListResponseDTO> getReviewList(QueryDTO req, CustomUserDetails principal) {
		
		// 쿼리DTO 가공
		QueryDTO normalized = nomarizedRequest(req, principal);
		
		// 목록 조회
		List<ReviewListResponseDTO> reviews = reviewMapper.getReviewList(normalized);
		
		// 조회한애들 No 뽑아서 키워드 조회하고 주입해
		attachKeyword(reviews);

		return reviews;
	}

	private void attachKeyword(List<ReviewListResponseDTO> reviews) {
		
		// 0. reviewNo빼내서 리스트화
		List<Long> reviewNos = reviews.stream()
        .map(ReviewListResponseDTO::getReviewNo)
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
		for (ReviewListResponseDTO review : reviews) {
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
			req.setMemberNo(null);
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
				"memberNo", principal.getMemberNo());
		
		return reviewMapper.getDetailReview(param);
	}

	@Override
	@Transactional
	public void deleteById(Long reviewNo, CustomUserDetails principal) {
		
		// 1. 게시글 상태 조회
		if(reviewMapper.existsReview(reviewNo) == 0) {
			throw new PostNotFoundException("조회된 게시글이 없습니다.");
		}
		
		
		// 2. 리뷰글 작성자 = 로그인 사용자?
		if(reviewMapper.getWriterById(reviewNo).equals(principal.getMemberNo())) {
			throw new AccessDeniedException("삭제 권한이 없습니다.");
		}
		
		// 3. 삭제 진행
		int result = reviewMapper.deleteById(reviewNo);
		
		if(result != 1) {
			throw new IllegalStateException("리뷰 삭제에 실패했습니다. 리뷰 번호를 확인해주세요.");
		}
	}
	
	@Override
	@Transactional
	public void create(ReviewCreateRequest review, CustomUserDetails principal) {
		
		// 유효성검사 review / principal
		
		// 식당 조회 -> 있으면 No반환 없으면 insert
		RestaurantRequestDTO requestRestaurant = RestaurantRequestDTO.builder()
				.normalName(review.getNormalName())
				.address(review.getAddress())
				.build();
		RestaurantResponseDTO restaurantResponse = reviewMapper.getRestaurantByName(requestRestaurant);
		Long restaurantNo;
		
		// 식당이 없다면 db생성
		if(restaurantResponse == null) {
			
			RestaurantCreateVO createRestaurant = RestaurantCreateVO.builder()
					.normalName(review.getNormalName())
					.address(review.getAddress())
					.latitude(review.getLatitude())
					.longitude(review.getLongitude())
					.build();
			
			log.info("normalName={}, address={}", createRestaurant.getNormalName(), createRestaurant.getAddress());
			
			reviewMapper.createRestaurant(createRestaurant);
			restaurantNo = createRestaurant.getRestaurantNo();
		} else {
			restaurantNo = restaurantResponse.getRestaurantNo();
		}
		
		ReviewCreateVO requestReview = ReviewCreateVO.builder()
				.memberNo(principal.getMemberNo())
				.restaurantNo(restaurantNo)
				.content(review.getContent())
				.rating(review.getRating())
				.build();
		
		reviewMapper.createReview(requestReview);
		Long reviewNo = requestReview.getReviewNo();
		
		reviewMapper.createReviewKeywordMap(reviewNo, review.getKeywordNos());
		
		// 파일 저장 -> URL 받고 -> DB 저장 슛
		List<String> uploadUrl = new ArrayList<>();
		
		try {
			
			if (review.getImages() != null) {
				
				int order = 1;
				
				for (MultipartFile f : review.getImages()) {
					
					if (f == null || f.isEmpty()) continue;
					
					String url = s3service.fileSave(f, "reviews/" + reviewNo);
					uploadUrl.add(url);
					
					reviewMapper.createReviewFile(
							ReviewFileCreateVO.builder()
							.reviewNo(reviewNo)
							.fileUrl(url)
							.sortOrder(order)
							.isThumbnail(order == 1 ? "Y" : "N")
							.build()
							);
					order++;
				}
			}
		} catch (Exception e) {
			for (String url : uploadUrl) {
				try { s3service.deleteFile(url); } catch (Exception ignore) {}
			}
			throw e;
		}
	}
	
	@Override
	public List<ReviewListResponseDTO> getMyReviewList(QueryDTO req, CustomUserDetails principal) {
	    // 1. 로그인 사용자 정보 및 기본값 세팅
	    req.setMemberNo(principal.getMemberNo());
	    if (req.getSize() == null || req.getSize() <= 0) req.setSize(10);
	    if (req.getCursor() == null || req.getCursor() <= 0) req.setCursor(1L); // cursor를 page 번호로 사용

	    // 2. 목록 조회
	    List<ReviewListResponseDTO> reviews = reviewMapper.getMyReviewList(req);

	    // 3. 키워드 연결 (결과가 있을 때만 실행하여 SQL 에러 방지)
	    if (reviews != null && !reviews.isEmpty()) {
	        attachKeyword(reviews);
	    }

	    return reviews;
	}

}
