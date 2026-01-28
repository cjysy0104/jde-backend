package com.kh.jde.admin.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class RankRequest {
	
    @NotBlank(message = "정책코드는 필수입니다.")
    @Size(max = 50, message = "정책코드는 50자 이하만 가능합니다.")
    @Pattern(regexp = "^[\\p{L}0-9_\\-]+$", message = "정책코드는 문자/숫자/언더스코어/하이픈만 가능합니다.")
    private String policyCode;

    @NotBlank(message = "정책이름은 필수입니다.")
    @Size(max = 100, message = "정책이름은 100자 이하만 가능합니다.")
    @Pattern(regexp = "^[\\s\\S]+$", message = "정책이름은 문자열이어야 합니다.")
    private String policyName;

    @Min(value = 0, message = "랭킹순위는 0 이상의 정수만 가능합니다.")
    private int topN;

    @Min(value = 0, message = "집계기간은 0 이상의 정수만 가능합니다.")
    private int periodDays;
    
    @Min(value = 0, message = "정책아이디는 0 이상의 정수만 가능합니다.")
    private int policyId;

    @NotBlank(message = "상태값은 필수입니다.")
    @Pattern(regexp = "^[YN]$", message = "상태값은 Y 또는 N만 가능합니다.")
    private String status;

    @NotNull(message = "설명은 null일 수 없습니다.")
    @Size(max = 500, message = "설명은 500자 이하만 가능합니다.")
    @Pattern(regexp = "^[\\s\\S]*$", message = "설명은 문자열이어야 합니다.")
    private String description;
    
}
