package com.kh.jde.member.model.dto;

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
public class CaptainDTO {
	
	private Long memberNo;
	private String nickname;
	private String fileUrl;
	private Long reviewCount;
	private Long likeCount;
	private int topN;
	
}
