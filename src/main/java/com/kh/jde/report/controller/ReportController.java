package com.kh.jde.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.CommentReportProcessDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportProcessDTO;
import com.kh.jde.report.model.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class ReportController {
	// 댓글 신고 전체 조회, 리뷰 신고 전체 조회 , 각 신고들 처리하기 로직, 각 신고들 상세 조회
	
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
	
	// 댓글 신고 페이징 조회
	@GetMapping("/comment")
	public ResponseEntity<SuccessResponse<ReportPageResponse<CommentReportListDTO>>> getCommentReportList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<CommentReportListDTO> reportPageResponse = reportService.getCommentReportList(page);
		
		return SuccessResponse.ok(reportPageResponse, "댓글 신고 목록 조회 성공");
	}
	
	// 리뷰 신고 페이징 조회
	@GetMapping("/review")
	public ResponseEntity<SuccessResponse<ReportPageResponse<ReviewReportListDTO>>> getReviewReportList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<ReviewReportListDTO> reportPageResponse = reportService.getReviewReportList(page);
		
		return SuccessResponse.ok(reportPageResponse, "리뷰 신고 목록 조회 성공");
	}
	
	// 댓글 신고 상세 조회
	@GetMapping("/comment/{reportNo}")
	public ResponseEntity<SuccessResponse<CommentReportListDTO>> getCommentReportByNo(
			@PathVariable(name="reportNo") Long reportNo){
		
		CommentReportListDTO commentReport = reportService.getCommentReportByNo(reportNo);
		
		return SuccessResponse.ok(commentReport, "댓글 신고 상세 조회 성공");
	}
	
	// 리뷰 신고 상세 조회
	@GetMapping("/review/{reportNo}")
	public ResponseEntity<SuccessResponse<ReviewReportListDTO>> getReviewReportByNo(
			@PathVariable(name="reportNo") Long reportNo){
		
		ReviewReportListDTO reviewReport = reportService.getReviewReportByNo(reportNo);
		
		return SuccessResponse.ok(reviewReport, "리뷰 신고 상세 조회 성공");
	}
	
	// 댓글 신고 처리
	@PutMapping("/comment/{reportNo}")
	public ResponseEntity<SuccessResponse<CommentReportListDTO>> processCommentReport(
			@PathVariable(name="reportNo") Long reportNo,
			@RequestBody CommentReportProcessDTO dto){
		
		dto.setReportNo(reportNo);
		CommentReportListDTO updatedReport = reportService.processCommentReport(dto);
		
		return SuccessResponse.ok(updatedReport, "댓글 신고가 처리되었습니다.");
	}
	
	// 리뷰 신고 처리
	@PutMapping("/review/{reportNo}")
	public ResponseEntity<SuccessResponse<ReviewReportListDTO>> processReviewReport(
			@PathVariable(name="reportNo") Long reportNo,
			@RequestBody ReviewReportProcessDTO dto){
		
		dto.setReportNo(reportNo);
		ReviewReportListDTO updatedReport = reportService.processReviewReport(dto);
		
		return SuccessResponse.ok(updatedReport, "리뷰 신고가 처리되었습니다.");
	}
}
