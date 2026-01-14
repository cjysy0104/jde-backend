package com.kh.jde.bookmark.model.service;

import java.util.List;

import com.kh.jde.bookmark.model.dto.BookmarkDTO;
import com.kh.jde.bookmark.model.dto.BookmarkToggleDTO;

public interface BookmarkService {

    BookmarkToggleDTO toggleBookmark(long memberNo, long reviewNo);

    // 무한스크롤: cursor 없으면 null
    List<BookmarkDTO> getMyBookmarks(long memberNo, int size, String cursor);
}
