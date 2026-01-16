package com.kh.jde.reviewlike.service;

import com.kh.jde.reviewlike.dto.ReviewLikeDTO;

public interface ReviewLikeService {
	
    ReviewLikeDTO like(Long reviewNo, Long memberNo);
    
    ReviewLikeDTO unlike(Long reviewNo, Long memberNo);
    
}
