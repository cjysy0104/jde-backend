package com.kh.jde.reviewlike.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.reviewlike.dao.ReviewLikeMapper;
import com.kh.jde.reviewlike.vo.ReviewLikeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeMapper reviewLikeMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean isReviewExists(Long reviewNo) {
        return reviewLikeMapper.existsReview(reviewNo) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAlreadyLiked(ReviewLikeVO reviewLike) {
        return reviewLikeMapper.existsLike(reviewLike) > 0;
    }

    @Override
    @Transactional
    public int like(ReviewLikeVO reviewLike) {
        return reviewLikeMapper.insertLike(reviewLike);
    }

    @Override
    @Transactional(readOnly = true)
    public int getLikeCount(Long reviewNo) {
        return reviewLikeMapper.countLikes(reviewNo);
    }
}
