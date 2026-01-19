package com.kh.jde.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.service.CommentService;
import com.kh.jde.common.responseData.SuccessResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	
	@GetMapping("/{reviewNo}")
	public ResponseEntity<SuccessResponse<List<CommentDTO>>> getCommentListById(@PathVariable("reviewNo")Long reviewNo){
		
		return SuccessResponse.ok(commentService.getCommentListById(reviewNo), "댓글 조회 성공");
	}
	
	@PostMapping("/{reviewNo}")
	public ResponseEntity<SuccessResponse<Void>> create(@PathVariable("reviewNo")Long reviewNo
															, @AuthenticationPrincipal CustomUserDetails principal
															, @RequestBody CommentDTO request){
		
		int result = commentService.create(reviewNo, principal, request);
		
		return SuccessResponse.created("댓글 등록이 완료되었습니다.");
	}
	
	@DeleteMapping("/{commentNo}")
	public ResponseEntity<SuccessResponse<Void>> deleteById(@PathVariable("commentNo")Long commentNo){
		
		return SuccessResponse.ok("댓글 삭제가 완료되었습니다.");
	}
}
