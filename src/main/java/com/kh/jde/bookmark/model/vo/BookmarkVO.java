package com.kh.jde.bookmark.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkVO {
    private Long memberNo;
    private Long reviewNo;
    
    // 페이징용
    private Integer offset;
    private Integer size;
}
