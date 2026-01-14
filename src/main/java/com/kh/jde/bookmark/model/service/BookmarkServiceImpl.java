package com.kh.jde.bookmark.model.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.bookmark.model.dao.BookmarkMapper;
import com.kh.jde.bookmark.model.dto.BookmarkDTO;
import com.kh.jde.bookmark.model.dto.BookmarkToggleDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public BookmarkToggleDTO toggleBookmark(long memberNo, long reviewNo) {

        int exists = bookmarkMapper.existsBookmark(memberNo, reviewNo);

        if (exists == 1) {
            bookmarkMapper.deleteBookmark(memberNo, reviewNo);
            return new BookmarkToggleDTO(false);
        } else {
            // 리뷰가 존재하는지 정도는 체크하는게 안전(원하면 추가)
            bookmarkMapper.insertBookmark(memberNo, reviewNo);
            return new BookmarkToggleDTO(true);
        }
    }

    @Override
    public List<BookmarkDTO> getMyBookmarks(long memberNo, int size, String cursor) {

        Timestamp cursorDate = null;
        Long cursorReviewNo = null;

        // cursor 형태: "밀리초타임스탬프|reviewNo"
        if (cursor != null && !cursor.isBlank()) {
            String[] parts = cursor.split("\\|");
            if (parts.length == 2) {
                cursorDate = new Timestamp(Long.parseLong(parts[0]));
                cursorReviewNo = Long.parseLong(parts[1]);
            }
        }

        List<BookmarkDTO> list =
                bookmarkMapper.selectMyBookmarks(memberNo, size, cursorDate, cursorReviewNo);

        // nextCursor 세팅 (리스트 마지막 기준)
        if (list != null && !list.isEmpty()) {
            BookmarkDTO last = list.get(list.size() - 1);
            if (last.getEnrollDate() != null && last.getReviewNo() != null) {
                String nextCursor = last.getEnrollDate().getTime() + "|" + last.getReviewNo();
                // 마지막 요소에만 넣어도 되고, 별도 응답 래핑해도 됨
                last.setNextCursor(nextCursor);
            }
        }

        return list;
    }
}
