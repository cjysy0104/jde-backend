package com.kh.jde.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentUpdateDTO;
import com.kh.jde.comment.model.vo.CommentVO;
import com.kh.jde.common.page.PageInfo;

@Mapper
public interface CommentMapper {
	
	CommentDTO existsComment(Long commentNo);

	List<CommentDTO> getCommentList(@Param("reviewNo")Long reviewNo, @Param("pi")PageInfo pi);

	int create(CommentVO comment);

	int deleteById(Long commentNo);

	int update(CommentUpdateDTO param);
	
	List<CommentDTO> getMyComments(com.kh.jde.review.model.dto.QueryDTO req);

	int getCommentCount(Long reviewNo);

}
