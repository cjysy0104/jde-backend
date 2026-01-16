package com.kh.jde.commentlike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.commentlike.model.dto.CommentLikeDTO;
import com.kh.jde.commentlike.model.service.CommentLikeService;
import com.kh.jde.common.responseData.SuccessResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/commentLikes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/{commentNo}")
    public ResponseEntity<?> createLike(@AuthenticationPrincipal CustomUserDetails user,
                                  @PathVariable("commentNo") Long commentNo) {
    	
    	log.info("HIT commentLike POST, commentNo={}", commentNo);
        CommentLikeDTO commentLike = commentLikeService.createLike(commentNo, user.getMemberNo());
        return SuccessResponse.created(commentLike, "댓글에 좋아요를 눌렀습니다.");
    }

    @DeleteMapping("/{commentNo}")
    public ResponseEntity<?> deleteLike(@AuthenticationPrincipal CustomUserDetails user,
                                    @PathVariable("commentNo") Long commentNo) {

        CommentLikeDTO commentLike = commentLikeService.deleteLike(commentNo, user.getMemberNo());
        return SuccessResponse.ok(commentLike, "댓글 좋아요를 취소했습니다.");
    }
}
