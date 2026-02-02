package com.kh.jde.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePhoneDTO {

    @NotBlank(message = "현재 비밀번호는 필수 입력사항입니다.")
    private String currentPassword;

    @Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    @NotBlank(message = "전화번호는 필수 입력사항입니다.")
    private String phone;
}
