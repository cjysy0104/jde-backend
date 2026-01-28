package com.kh.jde.bookmark.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkResponseDTO {
    private Long reviewNo;
    private Timestamp bookmarkEnrollDate;

    private String content;
    private Integer rating;
    private Timestamp reviewCreatedAt;

    private Long writerMemberNo;
    private String writerNickname;

    private Integer likeCount;
    private Integer commentCount;

    private String thumbnailUrl;
    private String restaurantName;
}
