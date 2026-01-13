package com.kh.jde.bookmark.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.bookmark.model.service.BookmarkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
	
	private final BookmarkService bookmarkService;

}
