package com.kh.jde.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeNicknameDTO {

    @NotBlank(message = "현재 비밀번호는 필수 입력사항입니다.")
    private String currentPassword;

    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "닉네임은 영어, 한글만 사용 가능합니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2글자 이상 10글자 이하만 사용할 수 있습니다.")
    @NotBlank(message = "닉네임은 필수 입력사항입니다.")
    private String nickname;
}
