package com.kh.jde.bookmark.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkToggleDTO {
	
	private boolean bookmarked; // true면 추가됨, false면 삭제됨
	
}
