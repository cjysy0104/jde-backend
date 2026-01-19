package com.kh.jde.comment.model.dto;

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
public class CommentDTO {

	private Long commentNo;
	private Long reviewNo;
	private String author;
	private String content;
	private int likeCount;
	private Date commentDate;
}
