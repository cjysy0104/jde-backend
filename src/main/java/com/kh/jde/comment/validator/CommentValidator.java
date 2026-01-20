package com.kh.jde.comment.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.common.constants.Status;
import com.kh.jde.exception.AccessDeniedException;
import com.kh.jde.exception.AlreadyDeletedException;
import com.kh.jde.exception.PostNotFoundException;
import com.kh.jde.exception.UnexpectedSQLResponseException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentValidator {

	// 댓글 존재 여부
	public CommentDTO validateCommentActive(CommentDTO comment) {
		
		if(comment == null) {
			throw new PostNotFoundException("댓글이 존재하지 않습니다.");
		}
		
		if(Status.DELETED.equals(comment.getStatus())) {
			throw new AlreadyDeletedException("이미 삭제된 댓글입니다.");
		}
		
		return comment;
	}
	
	// 자기꺼임?
	public void validateOwner(CommentDTO comment, CustomUserDetails principal) {
		
		validateAuthenticated(principal);
		
		if(!comment.getMemberNo().equals(principal.getMemberNo())) {
			throw new AccessDeniedException("댓글 수정/삭제 권한이 없습니다.");
		}
	}
	
	// SQL 성공함?
	public void validateResult(String action, int result) {
		
		if(result != 1) {
			throw new UnexpectedSQLResponseException(action + "에 실패했습니다.");
		}
	}

	// 로그인함?
	public void validateAuthenticated(CustomUserDetails principal) {

		if(principal == null) {
			throw new AccessDeniedException("로그인 정보가 없습니다.");
		}
	}

}
