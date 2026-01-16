package com.kh.jde.commentlike.model.service;

import com.kh.jde.commentlike.model.dto.CommentLikeDTO;

public interface CommentLikeService {
	
	CommentLikeDTO createLike(Long commentNo, Long memberNo);
	
    CommentLikeDTO deleteLike(Long commentNo, Long memberNo);
    
}
