package com.kh.jde.report.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.service.ReportService;
import com.kh.jde.report.model.vo.ReportCategoryVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class ReportController {
	// 댓글 신고 등록, 리뷰 신고 등록 (일반 사용자)
	
	private final ReportService reportService;
	
	//신고 카테고리
	@GetMapping("/categories")
	  public ResponseEntity<SuccessResponse<List<ReportCategoryVO>>> categories() {
	    return SuccessResponse.ok(reportService.getReportCategories());
	  }
	
	//댓글 신고 등록
	@PostMapping("/comment")
	public ResponseEntity<SuccessResponse<String>> createCommentReport(@AuthenticationPrincipal CustomUserDetails user,
			 @RequestBody CommentReportCreateDTO dto) {
		
		// 인증된 사용자 정보 가져오기
		dto.setMemberNo(user.getMemberNo());
		// 신고 등록
		reportService.createCommentReport(dto);
		
		return SuccessResponse.created("댓글 신고가 등록되었습니다.");
	}
	
	//리뷰 신고 등록
	@PostMapping("/review")
	public ResponseEntity<SuccessResponse<String>> createReviewReport(@AuthenticationPrincipal CustomUserDetails user,
		 @RequestBody ReviewReportCreateDTO dto) {
		
		// 인증된 사용자 정보 가져오기
		dto.setMemberNo(user.getMemberNo());
		// 신고 등록
		reportService.createReviewReport(dto);
		
		return SuccessResponse.created("리뷰 신고가 등록되었습니다.");
	}
	
}
