package com.kh.jde.admin.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DefaultImageDTO {
	
	private Long fileNo;
	private String fileName;
	private String fileUrl;
	private Date createdAt;
}
