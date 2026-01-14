package com.kh.jde.report.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommentReportProcessDTO {
	
	@NotNull(message = "신고 번호는 필수입니다.")
	private Long reportNo;
	
	@NotBlank(message = "처리 상태는 필수입니다.")
	private String reportProcess; // PENDING, IN_PROGRESS, RESOLVED, REJECTED
}
