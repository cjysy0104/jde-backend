package com.kh.jde.bookmark.model.service;

import java.util.List;

import com.kh.jde.bookmark.model.dto.BookmarkResponseDTO;
import com.kh.jde.bookmark.model.dto.BookmarkToggleDTO;

public interface BookmarkService {

	BookmarkToggleDTO toggle(Long memberNo, Long reviewNo);

    // 필요하면 프론트에서 "추가/삭제"를 따로 호출할 수도 있어서 열어둠
    void create(Long memberNo, Long reviewNo);
    void delete(Long memberNo, Long reviewNo);

    List<BookmarkResponseDTO> getMyBookmarks(Long memberNo, int page, int size);
}
