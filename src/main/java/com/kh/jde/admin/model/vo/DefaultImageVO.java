package com.kh.jde.admin.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DefaultImageVO {
	
	private Long fileNo;
	private String fileName;
	private String fileUrl;
	private Date createdAt;
}
