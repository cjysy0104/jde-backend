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
public class CommentListDTO {
	
	private Long commentNo;
	private Long memberNo;
	private String author; // 댓글 작성자 닉네임 (AUTHOR 필드)
	private Long reviewNo; // 댓글이 속한 리뷰 번호
	private String commentContent; // 댓글 내용
	private Date commentDate; // 댓글 작성일
	private String status;
}
