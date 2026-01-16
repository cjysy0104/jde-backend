package com.kh.jde.reviewlike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.ErrorResponse;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.reviewlike.dto.ReviewLikeDTO;
import com.kh.jde.reviewlike.service.ReviewLikeService;
import com.kh.jde.reviewlike.vo.ReviewLikeVO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviewLikes")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    // 리뷰 좋아요 등록
    @PostMapping("/{reviewNo}")
    public ResponseEntity<?> like(@AuthenticationPrincipal CustomUserDetails user,
                                  @PathVariable Long reviewNo,
                                  HttpServletRequest request) {

        String path = request.getRequestURI();
        Long memberNo = user.getMemberNo();

        // 1) 리뷰 존재 확인 (404)
        if (!reviewLikeService.isReviewExists(reviewNo)) {
            return ErrorResponse.notFound("요청한 리뷰를 찾을 수 없습니다.", path);
        }

        ReviewLikeVO reviewLike = new ReviewLikeVO(reviewNo, memberNo);

        // 2) 중복 좋아요 (400)
        if (reviewLikeService.isAlreadyLiked(reviewLike)) {
            return ErrorResponse.badRequest("이미 좋아요를 누른 리뷰입니다.", path);
        }

        // 3) 등록
        int inserted = reviewLikeService.like(reviewLike);
        if (inserted == 0) {
            return ErrorResponse.internalServerError("좋아요 처리 중 서버 오류가 발생했습니다.", path);
        }

        // 4) 응답 data
        int likeCount = reviewLikeService.getLikeCount(reviewNo);

        ReviewLikeDTO dto = ReviewLikeDTO.builder()
                .reviewNo(reviewNo)
                .isLiked(true)
                .likeCount(likeCount)
                .build();

        return SuccessResponse.created(dto, "리뷰에 좋아요를 눌렀습니다.");
    }
    
}