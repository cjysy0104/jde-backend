package com.kh.jde.reviewlike.service;

import com.kh.jde.reviewlike.dto.ReviewLikeDTO;

public interface ReviewLikeService {
	
    ReviewLikeDTO createLike(Long reviewNo, Long memberNo);
    
    ReviewLikeDTO deleteLike(Long reviewNo, Long memberNo);
    
}
