package com.kh.jde.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.comment.model.dto.CommentDTO;

@Mapper
public interface CommentMapper {
	
	int existsComment(Long commentNo);

	List<CommentDTO> getCommentList(Long reviewNo);
	
}
