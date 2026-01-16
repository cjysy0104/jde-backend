package com.kh.jde.reviewlike.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.review.model.dao.ReviewMapper;
import com.kh.jde.reviewlike.dao.ReviewLikeMapper;
import com.kh.jde.reviewlike.dto.ReviewLikeDTO;
import com.kh.jde.reviewlike.validator.LikeValidationSupport;
import com.kh.jde.reviewlike.vo.ReviewLikeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeMapper reviewLikeMapper;
    private final ReviewMapper reviewMapper;
    private final LikeValidationSupport validator;

    @Override
    @Transactional
    public ReviewLikeDTO createLike(Long reviewNo, Long memberNo) {
        ReviewLikeVO reviewLike = new ReviewLikeVO(reviewNo, memberNo);

        validateReviewExists(reviewNo);
        validator.validateNotAlreadyLiked(isAlreadyLiked(reviewLike), "이미 좋아요를 누른 리뷰입니다.");

        int inserted = reviewLikeMapper.createLike(reviewLike);
        validator.validateDbAffected(inserted, "좋아요 처리 중 서버 오류가 발생했습니다.");

        int likeCount = reviewLikeMapper.countLikes(reviewNo);
        return ReviewLikeDTO.builder()
                .reviewNo(reviewNo)
                .isLiked(true)
                .likeCount(likeCount)
                .build();
    }

    @Override
    @Transactional
    public ReviewLikeDTO deleteLike(Long reviewNo, Long memberNo) {
        ReviewLikeVO reviewLike = new ReviewLikeVO(reviewNo, memberNo);

        validateReviewExists(reviewNo);
        validator.validateAlreadyLiked(isAlreadyLiked(reviewLike), "좋아요를 누르지 않은 리뷰입니다.");

        int deleted = reviewLikeMapper.deleteLike(reviewLike);
        validator.validateDbAffected(deleted, "좋아요 취소 처리 중 서버 오류가 발생했습니다.");

        int likeCount = reviewLikeMapper.countLikes(reviewNo);
        return ReviewLikeDTO.builder()
                .reviewNo(reviewNo)
                .isLiked(false)
                .likeCount(likeCount)
                .build();
    }

    // ===== private validation helpers =====
    private void validateReviewExists(Long reviewNo) {
        boolean exists = reviewMapper.existsReview(reviewNo) > 0;
        validator.validateTargetExists(exists, "요청한 리뷰를 찾을 수 없습니다.");
    }

    private boolean isAlreadyLiked(ReviewLikeVO reviewLike) {
        return reviewLikeMapper.existsLike(reviewLike) > 0;
    }
}

