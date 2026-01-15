package com.kh.jde.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class ReportController {
	// 댓글 신고 등록, 리뷰 신고 등록 (일반 사용자)
	
	private final ReportService reportService;
	
	//댓글 신고 등록
	@PostMapping("/comment")
	public ResponseEntity<SuccessResponse<String>> createCommentReport(@AuthenticationPrincipal CustomUserDetails user,
			 @RequestBody CommentReportCreateDTO dto) {
		
		// 인증된 사용자 정보 가져오기
		//Long memberNo = user.getMemberNo();

		dto.setMemberNo((long)3);
		// 신고 등록
		reportService.createCommentReport(dto);
		
		return SuccessResponse.created("댓글 신고가 등록되었습니다.");
	}
	
	//리뷰 신고 등록
	@PostMapping("/review")
	public ResponseEntity<SuccessResponse<String>> createReviewReport(
		 @RequestBody ReviewReportCreateDTO dto) {
		
		// 인증된 사용자 정보 가져오기
		//Long memberNo = user.getMemberNo();
		
		dto.setMemberNo((long)3);
		// 신고 등록
		reportService.createReviewReport(dto);
		
		return SuccessResponse.created("리뷰 신고가 등록되었습니다.");
	}
	
}
