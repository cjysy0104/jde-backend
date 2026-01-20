package com.kh.jde.comment.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDTO {

	private Long commentNo;
	private Long reviewNo;
	private Long memberNo;
	private String nickname;
	private String content;
	private int likeCount;
	private Date commentDate;
	private String status;
}
