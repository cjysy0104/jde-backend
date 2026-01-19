package com.kh.jde.bookmark.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.jde.bookmark.model.dto.BookmarkResponseDTO;
import com.kh.jde.bookmark.model.vo.BookmarkVO;



@Mapper
public interface BookmarkMapper {

    int countBookmark(BookmarkVO key);

    int createBookmarkIgnoreDuplicate(BookmarkVO key);

    int deleteBookmark(BookmarkVO key);

    List<BookmarkResponseDTO> selectMyBookmarks(BookmarkVO param);
}