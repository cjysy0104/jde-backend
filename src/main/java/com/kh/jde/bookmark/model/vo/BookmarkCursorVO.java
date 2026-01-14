package com.kh.jde.bookmark.model.vo;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BookmarkCursorVO {
    private long memberNo;
    private int size;
    private Timestamp cursorDate;     // 마지막 ENROLL_DATE
    private Long cursorReviewNo;      // 마지막 REVIEW_NO
}
