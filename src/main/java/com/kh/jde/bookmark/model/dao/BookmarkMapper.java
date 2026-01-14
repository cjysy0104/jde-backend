package com.kh.jde.bookmark.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.jde.bookmark.model.dto.BookmarkResponseDTO;
import com.kh.jde.bookmark.model.vo.BookmarkVO;



@Mapper
public interface BookmarkMapper {

    int countBookmark(BookmarkVO key);

    int insertBookmarkIgnoreDuplicate(BookmarkVO key);
    // ↑ 중복방지: MERGE 안 쓰고 "INSERT ... WHERE NOT EXISTS" 로 처리

    int deleteBookmark(BookmarkVO key);

    List<BookmarkResponseDTO> selectMyBookmarks(
            @Param("memberNo") Long memberNo,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );
}