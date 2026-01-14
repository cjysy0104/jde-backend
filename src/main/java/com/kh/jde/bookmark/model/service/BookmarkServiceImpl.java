package com.kh.jde.bookmark.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.bookmark.model.dao.BookmarkMapper;
import com.kh.jde.bookmark.model.dto.BookmarkResponseDTO;
import com.kh.jde.bookmark.model.dto.BookmarkToggleDTO;
import com.kh.jde.bookmark.model.vo.BookmarkVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public BookmarkToggleDTO toggle(Long memberNo, Long reviewNo) {
        BookmarkVO key = BookmarkVO.builder()
                .memberNo(memberNo)
                .reviewNo(reviewNo)
                .build();

        int exists = bookmarkMapper.countBookmark(key);

        if (exists > 0) {
            bookmarkMapper.deleteBookmark(key);
            return BookmarkToggleDTO.builder()
                    .bookmarked(false)
                    .action("REMOVED")
                    .build();
        } else {
            // 중복 방지(예: 동시에 2번 눌러도 안전)
            bookmarkMapper.insertBookmarkIgnoreDuplicate(key);
            return BookmarkToggleDTO.builder()
                    .bookmarked(true)
                    .action("ADDED")
                    .build();
        }
    }

    @Override
    @Transactional
    public void add(Long memberNo, Long reviewNo) {
        BookmarkVO key = BookmarkVO.builder()
                .memberNo(memberNo)
                .reviewNo(reviewNo)
                .build();
        bookmarkMapper.insertBookmarkIgnoreDuplicate(key);
    }

    @Override
    @Transactional
    public void remove(Long memberNo, Long reviewNo) {
        BookmarkVO key = BookmarkVO.builder()
                .memberNo(memberNo)
                .reviewNo(reviewNo)
                .build();
        bookmarkMapper.deleteBookmark(key);
    }

    @Override
    public List<BookmarkResponseDTO> getMyBookmarks(Long memberNo, int page, int size) {
        int offset = page * size;

        BookmarkVO vo = BookmarkVO.builder()
                .memberNo(memberNo)
                .offset(offset)
                .size(size)
                .build();

        return bookmarkMapper.selectMyBookmarks(vo);
    }
}
