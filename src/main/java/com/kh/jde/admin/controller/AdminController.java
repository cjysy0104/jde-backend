package com.kh.jde.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.admin.model.service.AdminService;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.CommentReportProcessDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportProcessDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	// 댓글 신고 페이징 조회
	@GetMapping("/reports/comment")
	public ResponseEntity<SuccessResponse<ReportPageResponse<CommentReportListDTO>>> getCommentReportList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<CommentReportListDTO> reportPageResponse = adminService.getCommentReportList(page);
		
		return SuccessResponse.ok(reportPageResponse, "댓글 신고 목록 조회 성공");
	}
	
	// 리뷰 신고 페이징 조회
	@GetMapping("/reports/review")
	public ResponseEntity<SuccessResponse<ReportPageResponse<ReviewReportListDTO>>> getReviewReportList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<ReviewReportListDTO> reportPageResponse = adminService.getReviewReportList(page);
		
		return SuccessResponse.ok(reportPageResponse, "리뷰 신고 목록 조회 성공");
	}
	
	// 댓글 신고 상세 조회
	@GetMapping("/reports/comment/{reportNo}")
	public ResponseEntity<SuccessResponse<CommentReportListDTO>> getCommentReportByNo(
			@PathVariable(name="reportNo") Long reportNo){
		
		CommentReportListDTO commentReport = adminService.getCommentReportByNo(reportNo);
		
		return SuccessResponse.ok(commentReport, "댓글 신고 상세 조회 성공");
	}
	
	// 리뷰 신고 상세 조회
	@GetMapping("/reports/review/{reportNo}")
	public ResponseEntity<SuccessResponse<ReviewReportListDTO>> getReviewReportByNo(
			@PathVariable(name="reportNo") Long reportNo){
		
		ReviewReportListDTO reviewReport = adminService.getReviewReportByNo(reportNo);
		
		return SuccessResponse.ok(reviewReport, "리뷰 신고 상세 조회 성공");
	}
	
	// 댓글 신고 처리
	@PutMapping("/reports/comment/{reportNo}")
	public ResponseEntity<SuccessResponse<CommentReportListDTO>> processCommentReport(
			@PathVariable(name="reportNo") Long reportNo,
			@RequestBody CommentReportProcessDTO dto){
		
		dto.setReportNo(reportNo);
		CommentReportListDTO updatedReport = adminService.processCommentReport(dto);
		
		return SuccessResponse.ok(updatedReport, "댓글 신고가 처리되었습니다.");
	}
	
	// 리뷰 신고 처리
	@PutMapping("/reports/review/{reportNo}")
	public ResponseEntity<SuccessResponse<ReviewReportListDTO>> processReviewReport(
			@PathVariable(name="reportNo") Long reportNo,
			@RequestBody ReviewReportProcessDTO dto){
		
		dto.setReportNo(reportNo);
		ReviewReportListDTO updatedReport = adminService.processReviewReport(dto);
		
		return SuccessResponse.ok(updatedReport, "리뷰 신고가 처리되었습니다.");
	}

}
