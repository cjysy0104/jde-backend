package com.kh.jde.bookmark.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.bookmark.model.dto.BookmarkDTO;
import com.kh.jde.bookmark.model.dto.BookmarkToggleDTO;
import com.kh.jde.bookmark.model.service.BookmarkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크 토글 (추가/삭제)
    // POST /api/bookmarks/{reviewNo}
    @PostMapping("/{reviewNo}")
    public ResponseEntity<BookmarkToggleDTO> toggle(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable long reviewNo
    ) {
        long memberNo = principal.getMemberNo(); // 커스텀에 있어야 함
        return ResponseEntity.ok(bookmarkService.toggleBookmark(memberNo, reviewNo));
    }

    // 내 북마크 목록 무한스크롤
    // GET /api/bookmarks/me?size=12&cursor=...
    @GetMapping("/me")
    public ResponseEntity<List<BookmarkDTO>> myBookmarks(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String cursor
    ) {
        long memberNo = principal.getMemberNo();
        return ResponseEntity.ok(bookmarkService.getMyBookmarks(memberNo, size, cursor));
    }
}
