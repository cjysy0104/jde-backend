package com.kh.jde.commentlike.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.commentlike.model.vo.CommentLikeVO;

@Mapper
public interface CommentLikeMapper {
	
	int existsLike(CommentLikeVO commentLike);

    int createLike(CommentLikeVO commentLike);
    int deleteLike(CommentLikeVO commentLike);

    int countLikes(Long commentNo);
}
