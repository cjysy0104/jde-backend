package com.kh.jde.admin.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DefaultImageVO {
	
	private String fileName;
	private String fileUrl;

}
