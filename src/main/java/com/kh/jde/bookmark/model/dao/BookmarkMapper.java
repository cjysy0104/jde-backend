package com.kh.jde.bookmark.model.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.jde.bookmark.model.dto.BookmarkDTO;


@Mapper
public interface BookmarkMapper {

    int existsBookmark(@Param("memberNo") long memberNo,
                       @Param("reviewNo") long reviewNo);

    int insertBookmark(@Param("memberNo") long memberNo,
                       @Param("reviewNo") long reviewNo);

    int deleteBookmark(@Param("memberNo") long memberNo,
                       @Param("reviewNo") long reviewNo);

    List<BookmarkDTO> selectMyBookmarks(@Param("memberNo") long memberNo,
                                                @Param("size") int size,
                                                @Param("cursorDate") Timestamp cursorDate,
                                                @Param("cursorReviewNo") Long cursorReviewNo);
}