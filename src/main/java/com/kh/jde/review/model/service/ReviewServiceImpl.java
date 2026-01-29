package com.kh.jde.review.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.util.RequestNormalizer;
import com.kh.jde.exception.S3ServiceFailureException;
import com.kh.jde.file.service.S3Service;
import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.model.dto.BestReviewListResponse;
import com.kh.jde.review.model.dto.BestReviewPagingRequest;
import com.kh.jde.review.model.dto.CaptainQueryDTO;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.KeywordDTO;
import com.kh.jde.review.model.dto.KeywordRowDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.RestaurantRequestDTO;
import com.kh.jde.review.model.dto.RestaurantResponseDTO;
import com.kh.jde.review.model.dto.ReviewCreateRequest;
import com.kh.jde.review.model.dto.ReviewListResponseDTO;
import com.kh.jde.review.model.dto.ReviewUpdateRequest;
import com.kh.jde.review.model.vo.RestaurantCreateVO;
import com.kh.jde.review.model.vo.ReviewCreateVO;
import com.kh.jde.review.model.vo.ReviewFileCreateVO;
import com.kh.jde.review.model.vo.ReviewUpdateVo;
import com.kh.jde.review.validator.ReviewValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewMapper reviewMapper;
	private final S3Service s3service;
	private final ReviewValidator reviewValidator;
	private final RequestNormalizer requestNormalizer;

	@Override
	public List<ReviewListResponseDTO> getReviewList(QueryDTO request, CustomUserDetails principal) {
		
		// 쿼리DTO 가공
		QueryDTO normalized = nomarizedRequest(request, principal);
		
		// 목록 조회
		List<ReviewListResponseDTO> reviews = reviewMapper.getReviewList(normalized);
		
		return reviews;
	}
	
	@Override
	public List<ReviewListResponseDTO> getCaptainReviewList(QueryDTO request, CustomUserDetails principal, Long captainNo) {
		// 미식대장쿼리DTO 가공
		QueryDTO normalized = nomarizedRequest(request, principal);
		CaptainQueryDTO captainNormalized = new CaptainQueryDTO();
		captainNormalized.setCatptainNo(captainNo);
		captainNormalized.setQuery(normalized);
		
		// 미식대장의 리뷰 목록 조회
		List<ReviewListResponseDTO> captainReviews = reviewMapper.getCaptainReviewList(captainNormalized);
		
		return captainReviews;
	}
	
	private QueryDTO nomarizedRequest(QueryDTO request, CustomUserDetails principal) {

		// 0. query null 방지
		if(request == null) request = new QueryDTO();
		
		// 1. 로그인? 했으면 memberNo 주입 / 아니면 null
		if(principal != null) {
			request.setMemberNo(principal.getMemberNo());
		} else {
			request.setMemberNo(null);
		}
		
		// 2. scroll 만들자
		request.setScroll(requestNormalizer.applyScroll(request.getScroll(), 5)); 
		return request;
	}

	@Override
	@Transactional
	public DetailReviewDTO getDetailReview(Long reviewNo, CustomUserDetails principal) {
		
		// 0. 조회수 먼저 증가
		reviewMapper.increaseViewCount(reviewNo);

	    Map<String, Object> param = new HashMap<>();
	    param.put("reviewNo", reviewNo);
	    param.put("memberNo", principal == null ? null : principal.getMemberNo()); // 비로그인:


	    return reviewMapper.getDetailReview(param);
	}

	@Override
	@Transactional
	public void deleteById(Long reviewNo, CustomUserDetails principal) {
		
		// 1. 게시글 상태 조회
		getReviewOrThrow(reviewNo);
		
		
		// 2. 리뷰글 작성자 = 로그인 사용자 체크
		getWriterById(reviewNo, principal, "삭제");
		
		// 3. 삭제 진행
		int result = reviewMapper.deleteById(reviewNo);
		
		reviewValidator.validateResult(result);
	}
	
	private void getWriterById(Long reviewNo, CustomUserDetails principal, String action) {
		reviewValidator.validateWriter(reviewMapper.getWriterById(reviewNo), principal.getMemberNo(), action);
	}

	private void getReviewOrThrow(Long reviewNo) {
		reviewValidator.validateReviewExists(reviewMapper.getExistsReview(reviewNo));
	}

	@Override
	@Transactional
	public void create(ReviewCreateRequest review, CustomUserDetails principal) {
		
		
		// 유효성검사 principal
		
		
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
		
		// 파일 S3 저장 -> 반환받은 URL을 DB 저장
		createFiles(reviewNo, review.getImages());
		
		
	}
	
	// S3 저장 메서드
	private void createFiles(Long reviewNo, List<MultipartFile> images) {
		List<String> uploadUrl = new ArrayList<>();
		
		try {
			
			if (images != null) {
				
				int order = 1;
				
				for (MultipartFile f : images) {
					
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
			// 예외 발생 시: 이전까지 업로드한 uploadUrl 전부 삭제 처리(되돌림)
			for (String url : uploadUrl) {
				try { 
					s3service.deleteFile(url);
				} catch (Exception ignore) {
					// 실패 시 재시도
					deleteWitRetry(url, 5);
				}
			}
			throw e;
		}
	}

	// 파일 삭제 재시도 메서드
	private void deleteWitRetry(String url, int maxAttempt) {
		int attempt = 0;
		
		while(true) {
			// 삭제 성공 시 바로 돌아감
			try {
				s3service.deleteFile(url);
				return;
				// 실패 시 시도 횟수 count 하며 재시도, 특정 횟수 넘어가면 예외로 던져버리기
			} catch (Exception e) {
				attempt++;
				if(attempt >= maxAttempt) {
					e.printStackTrace();
					throw new S3ServiceFailureException(url + ": 파일 업로드에 실패했습니다.");
				}
			}
		}
	}

	@Override
	public List<ReviewListResponseDTO> getMyReviewList(QueryDTO req, CustomUserDetails principal) {
	    // 1. 로그인 사용자 정보 및 기본값 세팅
	    req.setMemberNo(principal.getMemberNo());
	    req.setScroll(requestNormalizer.applyScroll(req.getScroll(), 10));
	    if (req.getCursor() == null || req.getCursor() <= 0) req.setCursor(1L); // cursor를 page 번호로 사용

	    // 2. 목록 조회
	    List<ReviewListResponseDTO> reviews = reviewMapper.getMyReviewList(req);

	    return reviews;
	}

	@Override
	@Transactional
	public void update(Long reviewNo, ReviewUpdateRequest review, CustomUserDetails principal) {

		// 데이터 유효성 검사 하기
		getReviewOrThrow(reviewNo);
		// 작성자 여부 체크
		getWriterById(reviewNo, principal, "수정");
		
		// 리뷰글 db로 update
		ReviewUpdateVo requestReview = ReviewUpdateVo.builder()
													 .reviewNo(reviewNo)
													 .content(review.getContent())
													 .rating(review.getRating())
													 .build();
		reviewMapper.update(requestReview);
		
		// 키워드 삭제 후 다시 create
		reviewMapper.deleteKeywordsById(reviewNo); // 삭제
		reviewMapper.createReviewKeywordMap(reviewNo, review.getKeywordNos());
		
		
		
		// 파일 삭제 후 업로드
		// 1. 파일 삭제: URL 호출 해 s3 제거, db 제거
		// 2. 파일 업로드: 재사용
		List<String> legacyUrl = reviewMapper.getUrlById(reviewNo);
		
		// 1.
		try {
			for(String url : legacyUrl) {
				s3service.deleteFile(url);
				legacyUrl.remove(url);
			}
		} catch (Exception e) {
			for(String url : legacyUrl) {
				deleteWitRetry(url, 5);
			}
		}
		
		reviewMapper.deleteFilesById(reviewNo);
		
		// 2.
		createFiles(reviewNo, review.getImages());
		
		
	}

	@Override
	public List<BestReviewListResponse> getBestReviewList(BestReviewPagingRequest req) {
		
		req.setScroll(requestNormalizer.applyScroll(req.getScroll(), 3));
		
		List<BestReviewListResponse> reviews = reviewMapper.getBestReviewList(req);
		if(reviews == null || reviews.isEmpty()) {
			return reviews;
		}
		
		List<Long> reviewNos = reviews.stream()
                .map(BestReviewListResponse::getReviewNo)
                .toList();

        List<KeywordRowDTO> rows = reviewMapper.getKeywordsByIds(reviewNos);

        // 3) reviewNo별 keywords 그룹핑해서 주입
        Map<Long, List<KeywordDTO>> keywordMap = new HashMap<>();
        for (KeywordRowDTO row : rows) {
            keywordMap.computeIfAbsent(row.getReviewNo(), k -> new ArrayList<>())
                    .add(new KeywordDTO(row.getKeywordNo(), row.getKeywordName()));
        }

        for (BestReviewListResponse r : reviews) {
            r.setKeywords(keywordMap.getOrDefault(r.getReviewNo(), new ArrayList<>()));
        }
		
		return reviews;
	}

	@Override
	public List<KeywordDTO> getKeywordList() {
		return reviewMapper.getKeywordList();
	}


}
