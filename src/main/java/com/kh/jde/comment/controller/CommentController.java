package com.kh.jde.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
