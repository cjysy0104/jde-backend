package com.kh.jde.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DefaultProfileDTO {
    private Long fileNo;
    private String fileName;
    private String fileUrl;
}
