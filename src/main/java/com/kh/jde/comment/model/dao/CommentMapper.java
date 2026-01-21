package com.kh.jde.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentUpdateDTO;
import com.kh.jde.comment.model.vo.CommentVO;

@Mapper
public interface CommentMapper {
	
	CommentDTO existsComment(Long commentNo);

	List<CommentDTO> getCommentList(Long reviewNo);

	int create(CommentVO comment);

	int deleteById(Long commentNo);

	int update(CommentUpdateDTO param);
	
	List<CommentDTO> getMyComments(com.kh.jde.review.model.dto.QueryDTO req);

}
