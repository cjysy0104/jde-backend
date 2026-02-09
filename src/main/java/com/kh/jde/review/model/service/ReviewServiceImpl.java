package com.kh.jde.review.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.util.RequestNormalizer;
import com.kh.jde.exception.AccessDeniedException;
import com.kh.jde.exception.AlreadyDeletedException;
import com.kh.jde.exception.PostNotFoundException;
import com.kh.jde.exception.UsernameNotFoundException;
import com.kh.jde.file.service.S3FileManager;
import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.review.model.dto.BestReviewListResponse;
import com.kh.jde.review.model.dto.BestReviewPagingRequest;
import com.kh.jde.review.model.dto.CaptainQueryDTO;
import com.kh.jde.review.model.dto.DetailReviewDTO;
import com.kh.jde.review.model.dto.FileUpdateResult;
import com.kh.jde.review.model.dto.KeywordDTO;
import com.kh.jde.review.model.dto.KeywordRowDTO;
import com.kh.jde.review.model.dto.QueryDTO;
import com.kh.jde.review.model.dto.RestaurantRequestDTO;
import com.kh.jde.review.model.dto.RestaurantResponseDTO;
import com.kh.jde.review.model.dto.ReviewCreateRequest;
import com.kh.jde.review.model.dto.ReviewFileDTO;
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
	private final S3FileManager s3manager;
	private final ReviewValidator reviewValidator;
	private final RequestNormalizer requestNormalizer;
	
	private final int DEFAULT_SCROLL = 5;
	private final int RETRY_ATTEMPT = 5;
	

	/**
	 * 리뷰 글 전체 목록 검색
	 * 
	 * 검색 시 정보를 QueryDTO 형태로 받아 nomarizedRequest()를 통해 사용자 정보와 스크롤을 주입한 후 
	 * DB에 목록 조회를 요청합니다.
	 * @param request 검색어/필터, 로그인 사용자, 스크롤, 커서를 포함한 request 정보
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 * @return 조회해온 리뷰 글 전체 목록 ReviewListResponseDTO의 리스트 형태
	 */
	@Override
	public List<ReviewListResponseDTO> getReviewList(QueryDTO request, CustomUserDetails principal) {
		
		// 쿼리DTO 가공
		QueryDTO normalized = nomarizedRequest(request, principal);
		
		return reviewMapper.getReviewList(normalized);
	}
	
	/**
	 * (미식대장) 리뷰 글 전체 목록 검색
	 * 
	 * 검색 시 정보를 QueryDTO 형태로 받아 nomarizedRequest()를 통해 사용자 정보와 스크롤을 주입한 후 
	 * 추가로 CaptainQueryDTO를 생성해 미식대장의 Pk값과 기존 QueryDTO 값을 주입하여
	 * DB에 목록 조회를 요청합니다.
	 * @param request 검색어/필터, 로그인 사용자, 스크롤, 커서를 포함한 request 정보
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 * @param captainNo 조회하려는 미식대장의 PK 정보
	 * @return 조회해온 리뷰 글 전체 목록 ReviewListResponseDTO의 리스트 형태
	 */
	@Override
	public List<ReviewListResponseDTO> getCaptainReviewList(QueryDTO request, CustomUserDetails principal, Long captainNo) {
		
		// 검색쿼리DTO 가공
		QueryDTO normalized = nomarizedRequest(request, principal);
		
		// 미식대장쿼리DTO 생성: 조회하려는 미식대장 PK 및 Query 주입
		CaptainQueryDTO captainNormalized = new CaptainQueryDTO(captainNo, normalized);
		
		return reviewMapper.getCaptainReviewList(captainNormalized);
	}
	
	/**
	 * 검색 정보인 request를 받아 추가로 사용자 정보와 scroll 정보를 추가로 주입하여
	 * QueryDTO를 가공하는 메서드
	 * 
	 * @param request 검색어/필터, 로그인 사용자, 스크롤, 커서를 포함한 request 정보
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 * @return request 사용자 정보와 scroll이 주입되어 가공된 Query
	 */
	private QueryDTO nomarizedRequest(QueryDTO request, CustomUserDetails principal) {

		// 0. request 자체가 null값을 방지
		if(request == null) request = new QueryDTO();
		
		// 1. 사용자가 로그인 했으면 memberNo 주입 / 비로그인 시 null 주입
		request.setMemberNo(principal == null ? null : principal.getMemberNo());
		
		// 2. scroll 값 주입
		request.setScroll(requestNormalizer.applyScroll(request.getScroll(), DEFAULT_SCROLL)); 
		
		return request;
	}

	/**
	 * 리뷰 글 상세 조회
	 * 
	 * reviewNo를 통해 리뷰 글을 조회하여
	 * 요청받은 리뷰 글의 상세 정보: 게시글 정보, 작성자, 식당 정보, 이미지URL를 반환합니다.
	 * 조회 시 먼저 조회수 증가 메서드를 실행하며, 리뷰-reviewNo와 사용자 정보-memberNo를 
	 * Map<String, Object> 형태에 담아 파라미터로 사용하여 조회합니다.
	 * 
	 * @param reviewNo 게시글번호PK
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 * @return 조회해온 리뷰 글 전체 목록 ReviewListResponseDTO의 리스트 형태
	 */
	@Override
	@Transactional
	public DetailReviewDTO getDetailReview(Long reviewNo, CustomUserDetails principal) {
		
		// 0. 조회수 먼저 증가
		reviewMapper.increaseViewCount(reviewNo);

		// 1. 파라미터 가공
	    Map<String, Object> param = new HashMap<>();
	    param.put("reviewNo", reviewNo);
	    param.put("memberNo", principal == null ? null : principal.getMemberNo());


	    return reviewMapper.getDetailReview(param);
	}

	/**
	 * 리뷰 글 삭제 (소프트)
	 * 
	 * reviewNo를 통해 게시글 상태와 작성자 여부 체크 등 유효성 검사 후 
	 * 정상적으로 리뷰 글을 삭제(STATUS='N'으로 UPDATE)합니다.
	 * 
	 * @param reviewNo 게시글번호PK
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 * @throws PostNotFoundException: 조회해온 review가 null, 조회된 결과가 없을 경우 
	 * @throws AlreadyDeletedException: 조회해온 review의 필드 status가 n, 삭제된 게시물을 조회해온 경우
	 * @throws AccessDeniedException: 조회해온 review의 필드 writer와 principal의 memberNo가 불일치, 작성자가 아닌 경우
	 * @throws UsernameNotFoundException: 조회해온 review의 필드 writer가 null, 작성자를 찾을 수 없는 경우
	 */
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
		
		/* 
		 * 소프트 삭제이므로 파일 삭제는 제외
		 * 
		// 4. 파일 조회
		List<ReviewFileDTO> originFiles = reviewMapper.getFilesById(reviewNo);
	    
		// 5. s3 파일 삭제
		List<String> urls = originFiles.stream()
		        .map(ReviewFileDTO::getFileUrl)
		        .collect(Collectors.toList());

		
		s3manager.deleteUrls(urls, RETRY_ATTEMPT);
		*/
	}
	
	
	private void getWriterById(Long reviewNo, CustomUserDetails principal, String action) {
		reviewValidator.validateWriter(reviewMapper.getWriterById(reviewNo), principal.getMemberNo(), action);
	}

	private void getReviewOrThrow(Long reviewNo) {
		reviewValidator.validateReviewExists(reviewMapper.getExistsReview(reviewNo));
	}

	/**
	 * 리뷰 글 등록
	 * 
	 * 요청받은 review의 유효성 검사와 
	 * review의 식당 정보가 현재 DB에 저장되어있는지 조회하여 결과값이 없다면 식당 정보를 DB에 INSERT 작업을 먼저 진행합니다.
	 * VO에 담아 review 글을 DB에 INSERT 합니다.
	 * 이후 해당 리뷰에 매칭되는 키워드 정보를 DB에 INSERT 합니다.
	 * DB 작업이 모두 수행되면 S3에 파일 업로드를 실행합니다.
	 * 
	 * @param review ReviewCreateRequest형태 review: 본문 정보, 식당 정보 및 MultipartFile 파일 리스트를 담습니다.
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 */
	@Override
	@Transactional
	public void create(ReviewCreateRequest review, CustomUserDetails principal) {
		
		
		// 유효성검사 principal
		reviewValidator.validateReview(review);
		
		// 식당 조회 -> 있으면 No반환 없으면 INSERT
		Long restaurantNo = getRestaurantOrCreate(review);
		
		// 게시글 등록 INSERT
		ReviewCreateVO requestReview = ReviewCreateVO.builder()
													.memberNo(principal.getMemberNo())
													.restaurantNo(restaurantNo)
													.content(review.getContent())
													.rating(review.getRating())
													.build();
		
		reviewMapper.createReview(requestReview);
		
		Long reviewNo = requestReview.getReviewNo();
		
		reviewMapper.createReviewKeywordMap(reviewNo, review.getKeywordNos());
		
		// 파일 S3 저장
		List<String> uploadUrls = s3manager.createFiles("review", reviewNo, review.getImages(), RETRY_ATTEMPT);
		
		// DB에 파일 URL 저장 INSERT
		createFiles(reviewNo, uploadUrls);
		
		
	}
	
	private Long getRestaurantOrCreate(ReviewCreateRequest review) {
		
		RestaurantRequestDTO requestRestaurant = RestaurantRequestDTO.builder()
				.normalName(review.getNormalName())
				.address(review.getAddress())
				.build();
		
		RestaurantResponseDTO restaurantResponse = reviewMapper.getRestaurantByName(requestRestaurant);
		
		if(restaurantResponse == null) createRestaurant(review);
		
		return restaurantResponse.getRestaurantNo();
	}
	
	private Long createRestaurant(ReviewCreateRequest review) {
		
		RestaurantCreateVO createRestaurant = RestaurantCreateVO.builder()
																.normalName(review.getNormalName())
																.address(review.getAddress())
																.latitude(review.getLatitude())
																.longitude(review.getLongitude())
																.build();
		
		reviewMapper.createRestaurant(createRestaurant);
		
		return createRestaurant.getRestaurantNo();
	}

	private void createFiles(Long reviewNo, List<String> urls) {
		try {
			int order = 1;
			
			for(String url : urls) {
				if(url == null || url.isEmpty()) continue;
				
				reviewMapper.createReviewFile(ReviewFileCreateVO.builder()
																.reviewNo(reviewNo)
																.fileUrl(url)
																.sortOrder(order)
																.isThumbnail(order == 1 ? "Y" : "N")
																.build());
				order++;
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	/**
	 * 리뷰 글 수정
	 * 
	 * 수정 요청 글No와 리뷰 content, 남겨진 기존파일, 새롭게 업로드된 파일 정보가 담긴 ReviewUpdateRequest받습니다.
	 * review 유효성과 작성자 여부 체크 후 
	 * 해당 리뷰 글의 UPDATE를 진행합니다.
	 * 이후 해당 리뷰에 해당하는 키워드 매핑을 DELETE 후 다시 CREATE 합니다.
	 * 이후 파일 수정 처리를 진행합니다.
	 * 
	 * @param reviewNo 게시글번호PK
	 * @param review 요청: content 등 수정될 리뷰 본문 정보와 유지될 기존파일의 URL, 새롭게 생성될 파일 및 정렬순서가 담겨있습니다.
	 * @param principal 사용자 정보, 비로그인의 경우 null 값
	 */
	@Override
	@Transactional
	public void update(Long reviewNo, ReviewUpdateRequest review, CustomUserDetails principal) {
		
		// 유효성 검사 및 권한 체크
		validateReviewUpdate(reviewNo, principal);
		
		// 리뷰 본문 업데이트
		updateReviewContent(reviewNo, review);
		
		// 키워드 업데이트
		updateKeywords(reviewNo, review.getKeywordNos());
		
		// 파일 업데이트
		updateFiles(reviewNo, review);
	}
	
	private void validateReviewUpdate(Long reviewNo, CustomUserDetails principal) {
		// 데이터 유효성 검사 하기
		getReviewOrThrow(reviewNo);
		// 작성자 여부 체크
		getWriterById(reviewNo, principal, "수정");
	}

	private void updateReviewContent(Long reviewNo, ReviewUpdateRequest review) {
		ReviewUpdateVo requestReview = ReviewUpdateVo.builder()
				.reviewNo(reviewNo)
				.content(review.getContent())
				.rating(review.getRating())
				.build();
		reviewMapper.update(requestReview);
	}
	
	private void updateKeywords(Long reviewNo, List<Long> keywordNos) {
		reviewMapper.deleteKeywordsById(reviewNo); // 기존 Keywords 전체 DELETE
		reviewMapper.createReviewKeywordMap(reviewNo, keywordNos); // Keywords INSERT
	}
	
	/**
	 * 파일 수정 처리
	 * 
	 * 먼저 새로운 파일을 S3에 업로드하여 URL을 확보합니다.
	 * 이후 DB에 기존 파일을 조회해 비교하여 
	 * 유지 삭제 생성할 파일을 분류합니다.
	 * 분류한 파일을 DB에 갱신합니다.
	 * 모든 DB 작업을 마치면 S3에 파일 삭제를 요청합니다.
	 * 
	 * @param reviewNo 게시글번호PK
	 * @param review 요청: content 등 수정될 리뷰 본문 정보와 유지될 기존파일의 URL, 새롭게 생성될 파일 및 정렬순서가 담겨있습니다.
	 */
	private void updateFiles(Long reviewNo, ReviewUpdateRequest review) {
		// 1. 새로운 파일 S3에 업로드
		List<String> newFileUrls = s3manager.createFiles("review", reviewNo, review.getImages(), RETRY_ATTEMPT);
		
		// 2. db에 기존파일 정보 배열 받기 SELECT
		List<ReviewFileDTO> originFiles = reviewMapper.getFilesById(reviewNo);
		
		// 3. 파일 분류
		FileUpdateResult fileUpdateResult = classifyFiles(originFiles, review, newFileUrls);
		
		// 4. DB 갱신
		refreshFileRecords(reviewNo, fileUpdateResult.getFilesToKeep());
		
		// 5. S3 삭제
		deleteFilesFromS3(fileUpdateResult.getFilesToDelete());
	}
	
	private void deleteFilesFromS3(List<ReviewFileDTO> filesToDelete) {
		// S3 삭제
		List<String> deleteUrls = filesToDelete.stream()
												.map(ReviewFileDTO::getFileUrl)
												.collect(Collectors.toList());
		s3manager.deleteUrls(deleteUrls, RETRY_ATTEMPT);
	}

	private void refreshFileRecords(Long reviewNo, List<ReviewFileDTO> filesToKeep) {
		// DELETE
		reviewMapper.deleteFilesById(reviewNo);
		
		List<String> updateUrls = filesToKeep.stream()
										.map(ReviewFileDTO::getFileUrl)
										.collect(Collectors.toList());
		
		// INSERT
		createFiles(reviewNo, updateUrls);		
	}

	private FileUpdateResult classifyFiles(List<ReviewFileDTO> originFiles,
										ReviewUpdateRequest review,
										List<String> newFileUrls) {
		// 배열과 비교하여 삭제할 파일 배열 생성 및 살릴 파일 배열생성 후 
		// 기존파일의URL과 새파일URL을 sortOrder 순서대로 조립
		List<Long> reqExistingNos = orEmpty(review.getExistingFileNos());
		List<Integer> reqExistingOrders = orEmpty(review.getExistingSortOrders());
		List<Integer> reqNewOrders = orEmpty(review.getNewSortOrders());
		
		List<ReviewFileDTO> exists = new ArrayList<>();
		for(int i = 0; i < reqExistingNos.size(); i++) {
			exists.add(new ReviewFileDTO(
						reqExistingNos.get(i)
						, null
						, reqExistingOrders.get(i)
						)
					);
		}
		
		List<ReviewFileDTO> toUpdate = new ArrayList<>();
		
		for(int i = 0; i < reqNewOrders.size(); i++) {
			toUpdate.add(new ReviewFileDTO(
							null
							, newFileUrls.get(i)
							, reqNewOrders.get(i)
						)
					);
		}
		
		// 비교
		Map<Long, ReviewFileDTO> existByNo = exists.stream()
													.filter(e -> e.getFileNo() != null)
													.collect(Collectors.toMap(ReviewFileDTO::getFileNo, Function.identity()));
		
        List<ReviewFileDTO> toKeep = new ArrayList<>();
        List<ReviewFileDTO> toDelete = new ArrayList<>();
		
		for(ReviewFileDTO origin : originFiles) {
			ReviewFileDTO req = existByNo.get(origin.getFileNo());
			
			if(req != null) {
				toKeep.add(new ReviewFileDTO(origin.getFileNo(),
											origin.getFileUrl(),
											req.getSortOrder()
							)
						);
			} else {
				toDelete.add(origin);
			}
		}
		
		toUpdate.addAll(toKeep);
		
		return new FileUpdateResult(toKeep, toDelete);
	}
	

	private <T> List<T> orEmpty(List<T> list) {
	    return list == null ? List.of() : list;
	}

	/**
	 * 베스트리뷰(오늘의리뷰) 글 전체 조회
	 * 
	 * 키워드에 따른 베스트 리뷰 글을 전체 조회합니다.
	 * 스크롤 정보 주입 후 글을 조회합니다. 조회해온 글에 reviewNO별 키워드를 조회해 그룹핑해 주입하여 반환합니다.
	 * 
	 * @param req 기본적인 커서, 스크롤 정보와 사용자가 택할 키워드를 담습니다.
	 */
	@Override
	public List<BestReviewListResponse> getBestReviewList(BestReviewPagingRequest req) {
		
		req.setScroll(requestNormalizer.applyScroll(req.getScroll(), 3));
		
		List<BestReviewListResponse> reviews = reviewMapper.getBestReviewList(req);
		if(reviews == null || reviews.isEmpty()) {
			return reviews;
		}
		
		List<Long> reviewNos = reviews.stream()
                .map(BestReviewListResponse::getReviewNo)
                .collect(Collectors.toList());

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
