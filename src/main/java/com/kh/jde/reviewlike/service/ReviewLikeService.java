package com.kh.jde.reviewlike.service;

import com.kh.jde.reviewlike.vo.ReviewLikeVO;

public interface ReviewLikeService {

    boolean isReviewExists(Long reviewNo);

    boolean isAlreadyLiked(ReviewLikeVO reviewLike);

    int like(ReviewLikeVO reviewLike);

    int getLikeCount(Long reviewNo);
}
