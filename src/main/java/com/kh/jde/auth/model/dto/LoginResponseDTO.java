package com.kh.jde.auth.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {

    private String accessToken;
    private String refreshToken;

    private String email;
    private String memberName;
    private String nickname;
    private String phone;
    private Long memberNo;
    private Date enrollDate;
    private String role;
}
