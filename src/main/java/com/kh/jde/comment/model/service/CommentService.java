package com.kh.jde.comment.model.service;

import java.util.List;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentRequestDTO;
import com.kh.jde.comment.model.dto.CommentResponse;

public interface CommentService {

	CommentResponse getCommentListById(Long reviewNo, int page, CustomUserDetails principal);

	void create(Long reviewNo, CustomUserDetails principal, CommentRequestDTO request);
	
	void deleteById(Long commentNo, CustomUserDetails principal);

	void update(Long commentNo, CustomUserDetails principal, CommentRequestDTO request);
	
	List<CommentDTO> getMyComments(com.kh.jde.review.model.dto.QueryDTO req, CustomUserDetails principal);

}
