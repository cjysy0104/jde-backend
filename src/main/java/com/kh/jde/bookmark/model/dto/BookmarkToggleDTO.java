package com.kh.jde.bookmark.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookmarkToggleDTO {
    private boolean bookmarked;   // true면 추가된 상태, false면 삭제된 상태
    private String action;        // "ADDED" / "REMOVED"
}
