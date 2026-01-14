package com.kh.jde.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.bookmark.model.dto.BookmarkResponseDTO;
import com.kh.jde.bookmark.model.dto.BookmarkToggleDTO;
import com.kh.jde.bookmark.model.service.BookmarkService;
import com.kh.jde.common.responseData.SuccessResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 리뷰 상세/목록에서 북마크 버튼 클릭 -> 토글
     * - bookmarked=true  => 추가됨
     * - bookmarked=false => 삭제됨
     */
    @PostMapping("/{reviewNo}/toggle")
    public ResponseEntity<SuccessResponse<BookmarkToggleDTO>> toggle(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("reviewNo") Long reviewNo) {
        Long memberNo = user.getMemberNo();
        return SuccessResponse.ok(bookmarkService.toggle(memberNo, reviewNo), "북마크 토글 성공!");
    }

    /** (옵션) 북마크 추가 - idempotent */
    @PostMapping("/{reviewNo}")
    public ResponseEntity<SuccessResponse<Void>> add(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("reviewNo") Long reviewNo) {
        Long memberNo = user.getMemberNo();
        bookmarkService.add(memberNo, reviewNo);
        return SuccessResponse.ok(null, "북마크 추가 성공!");
    }

    /** (옵션) 북마크 삭제 - idempotent */
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<SuccessResponse<Void>> remove(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("reviewNo") Long reviewNo) {
        Long memberNo = user.getMemberNo();
        bookmarkService.remove(memberNo, reviewNo);
        return SuccessResponse.ok(null, "북마크 삭제 성공!");
    }

    /**
     * 마이페이지 - 내 북마크 목록
     * 예) GET /api/bookmarks/me?page=0&size=20
     */
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<BookmarkResponseDTO>> myBookmarks(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        Long memberNo = user.getMemberNo();
        return SuccessResponse.ok(bookmarkService.getMyBookmarks(memberNo, page, size), "북마크 조회 성공!");
    }
}
