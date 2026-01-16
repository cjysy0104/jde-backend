package com.kh.jde.reviewlike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.commentlike.model.dto.CommentLikeDTO;
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

    @PostMapping("/{reviewNo}")
    public ResponseEntity<SuccessResponse<ReviewLikeDTO>> createLike(@AuthenticationPrincipal CustomUserDetails user,
                                  @PathVariable("reviewNo") Long reviewNo) {

        ReviewLikeDTO reviewLike = reviewLikeService.createLike(reviewNo, user.getMemberNo());
        return SuccessResponse.created(reviewLike, "리뷰에 좋아요를 눌렀습니다.");
    }

    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<SuccessResponse<ReviewLikeDTO>> deleteLike(@AuthenticationPrincipal CustomUserDetails user,
                                    @PathVariable("reviewNo") Long reviewNo) {

        ReviewLikeDTO reviewLike = reviewLikeService.deleteLike(reviewNo, user.getMemberNo());
        return SuccessResponse.ok(reviewLike, "리뷰 좋아요를 취소했습니다.");
    }
}
