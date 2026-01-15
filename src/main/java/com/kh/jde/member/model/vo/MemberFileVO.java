package com.kh.jde.member.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFileVO {
    private Long memberNo;
    private String fileUrl;
}
