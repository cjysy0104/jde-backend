package com.kh.jde.comment.model.service;

import java.util.List;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dto.CommentDTO;

public interface CommentService {

	List<CommentDTO> getCommentListById(Long reviewNo);

	int create(Long reviewNo, CustomUserDetails principal, CommentDTO request);
	
	int deleteById(Long commentNo);
	
	

}
