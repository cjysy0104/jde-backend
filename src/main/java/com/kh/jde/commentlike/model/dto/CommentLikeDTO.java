package com.kh.jde.commentlike.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeDTO {
    private Long commentNo;
    private boolean isLiked;
    private int likeCount;
}
