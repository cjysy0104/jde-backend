package com.kh.jde.comment.model.dto;

import java.util.List;

import com.kh.jde.common.page.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

	private List<CommentDTO> comments;
	private PageInfo pageInfo;
}
