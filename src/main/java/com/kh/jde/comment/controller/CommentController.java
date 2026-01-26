package com.kh.jde.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.comment.model.dto.CommentDTO;
import com.kh.jde.comment.model.dto.CommentRequestDTO;
import com.kh.jde.comment.model.dto.CommentResponse;
import com.kh.jde.comment.model.service.CommentService;
import com.kh.jde.common.responseData.SuccessResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	
	@GetMapping("/{reviewNo}")
	public ResponseEntity<SuccessResponse<CommentResponse>> getCommentListById(@PathVariable("reviewNo")Long reviewNo,
																				@RequestParam(name = "currentPage", defaultValue = "1")int currentPage){
		
		return SuccessResponse.ok(commentService.getCommentListById(reviewNo, currentPage), "댓글 조회 성공");
	}
	
	@PostMapping("/{reviewNo}")
	public ResponseEntity<SuccessResponse<Void>> create(@PathVariable("reviewNo")Long reviewNo
														, @AuthenticationPrincipal CustomUserDetails principal
														, @RequestBody @Valid CommentRequestDTO request){
		
		commentService.create(reviewNo, principal, request);
		
		return SuccessResponse.created("댓글 등록이 완료되었습니다.");
	}
	
	@DeleteMapping("/{commentNo}")
	public ResponseEntity<SuccessResponse<Void>> deleteById(@PathVariable("commentNo")Long commentNo
														  , @AuthenticationPrincipal CustomUserDetails principal){
		
		commentService.deleteById(commentNo, principal);
		
		return SuccessResponse.ok("댓글 삭제가 완료되었습니다.");
	}
	
	@PatchMapping("/{commentNo}")
	public ResponseEntity<SuccessResponse<Void>> update(@PathVariable("commentNo")Long commentNo
														, @AuthenticationPrincipal CustomUserDetails principal
														, @RequestBody @Valid CommentRequestDTO request){
		
		commentService.update(commentNo, principal, request);
		
		return SuccessResponse.ok("댓글 수정이 완료되었습니다.");
	}
	
	@GetMapping("/me")
	public ResponseEntity<SuccessResponse<List<CommentDTO>>> getMyComments(
	        @ModelAttribute com.kh.jde.review.model.dto.QueryDTO req,
	        @AuthenticationPrincipal CustomUserDetails principal
	) {
	    if (principal == null) throw new com.kh.jde.exception.AccessDeniedException("로그인이 필요합니다.");

	    return SuccessResponse.ok(commentService.getMyComments(req, principal), "내 댓글 조회 성공");
	}

	
}
