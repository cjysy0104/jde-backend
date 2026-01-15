package com.kh.jde.admin.model.service;

import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.CommentReportProcessDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportProcessDTO;

public interface AdminService {
	
	// 댓글 신고 페이징 조회
	ReportPageResponse<CommentReportListDTO> getCommentReportList(int currentPage);
	
	// 리뷰 신고 페이징 조회
	ReportPageResponse<ReviewReportListDTO> getReviewReportList(int currentPage);

	// 댓글 신고 상세 조회
	CommentReportListDTO getCommentReportByNo(Long reportNo);
	
	// 리뷰 신고 상세 조회
	ReviewReportListDTO getReviewReportByNo(Long reportNo);
	
	// 댓글 신고 처리
	CommentReportListDTO processCommentReport(CommentReportProcessDTO dto);
	
	// 리뷰 신고 처리
	ReviewReportListDTO processReviewReport(ReviewReportProcessDTO dto);
}
