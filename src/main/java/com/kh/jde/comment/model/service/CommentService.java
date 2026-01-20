package com.kh.jde.comment.model.service;

import java.util.List;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentRequestDTO;

public interface CommentService {

	List<CommentDTO> getCommentListById(Long reviewNo);

	void create(Long reviewNo, CustomUserDetails principal, CommentRequestDTO request);
	
	void deleteById(Long commentNo, CustomUserDetails principal);

	void update(Long commentNo, CustomUserDetails principal, CommentRequestDTO request);
	
	

}
