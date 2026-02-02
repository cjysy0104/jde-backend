package com.kh.jde.file.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
	
	List<String> getDefaultImage();
	
	String getfileUrl(Long memberNo);
}
