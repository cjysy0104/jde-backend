package com.kh.jde.commentlike.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.commentlike.model.service.CommentLikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/commentLike")
@RequiredArgsConstructor
public class CommentLikeController {
	
	private final CommentLikeService commentLikeService;
	
	
	
	
	
}
