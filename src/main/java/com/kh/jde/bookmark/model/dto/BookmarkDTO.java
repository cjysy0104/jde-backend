package com.kh.jde.bookmark.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDTO {
    private Long reviewNo;
    private Date enrollDate;

    // 화면용(필요한 만큼만)
    private String reviewContent;
    private Integer rating;
    private Long viewCount;

    // 썸네일 1장만(원하면 조인 더)
    private String thumbnailUrl;

    // 무한스크롤 커서용(다음 호출에 내려줄 값)
    private String nextCursor; // "timestamp|reviewNo" 형태로 내려줌
}
