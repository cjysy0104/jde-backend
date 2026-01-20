package com.kh.jde.reviewlike.model.service;

import com.kh.jde.reviewlike.model.dto.ReviewLikeDTO;

public interface ReviewLikeService {
	
    ReviewLikeDTO createLike(Long reviewNo, Long memberNo);
    
    ReviewLikeDTO deleteLike(Long reviewNo, Long memberNo);
    
}
