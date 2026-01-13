package com.kh.jde.bookmark.model.service;

import org.springframework.stereotype.Service;

import com.kh.jde.bookmark.model.dao.BookmarkMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	
	private final BookmarkMapper bookmarkMapper;

}
