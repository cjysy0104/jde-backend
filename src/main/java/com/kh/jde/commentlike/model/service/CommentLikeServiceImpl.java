package com.kh.jde.commentlike.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.comment.model.dao.CommentMapper;
import com.kh.jde.commentlike.model.dao.CommentLikeMapper;
import com.kh.jde.commentlike.model.dto.CommentLikeDTO;
import com.kh.jde.commentlike.model.vo.CommentLikeVO;
import com.kh.jde.reviewlike.validator.LikeValidationSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeMapper commentLikeMapper;
    private final CommentMapper commentMapper;
    private final LikeValidationSupport validator;

    @Override
    @Transactional
    public CommentLikeDTO createLike(Long commentNo, Long memberNo) {
        CommentLikeVO commentLike = new CommentLikeVO(commentNo, memberNo);

        validateCommentExists(commentNo);
        validator.validateNotAlreadyLiked(isAlreadyLiked(commentLike), "이미 좋아요를 누른 댓글입니다.");

        int inserted = commentLikeMapper.createLike(commentLike);
        validator.validateDbAffected(inserted, "댓글 좋아요 처리 중 서버 오류가 발생했습니다.");

        int likeCount = commentLikeMapper.countLikes(commentNo);
        return CommentLikeDTO.builder()
                .commentNo(commentNo)
                .isLiked(true)
                .likeCount(likeCount)
                .build();
    }

    @Override
    @Transactional
    public CommentLikeDTO deleteLike(Long commentNo, Long memberNo) {
        CommentLikeVO commentLike = new CommentLikeVO(commentNo, memberNo);

        validateCommentExists(commentNo);
        validator.validateAlreadyLiked(isAlreadyLiked(commentLike), "좋아요를 누르지 않은 댓글입니다.");

        int deleted = commentLikeMapper.deleteLike(commentLike);
        validator.validateDbAffected(deleted, "댓글 좋아요 취소 처리 중 서버 오류가 발생했습니다.");

        int likeCount = commentLikeMapper.countLikes(commentNo);
        return CommentLikeDTO.builder()
                .commentNo(commentNo)
                .isLiked(false)
                .likeCount(likeCount)
                .build();
    }

    // ===== private validation helpers =====
    private void validateCommentExists(Long commentNo) {
        boolean exists = commentMapper.existsComment(commentNo) > 0;
        validator.validateTargetExists(exists, "요청한 댓글을 찾을 수 없습니다.");
    }

    private boolean isAlreadyLiked(CommentLikeVO commentLike) {
        return commentLikeMapper.existsLike(commentLike) > 0;
    }
}
