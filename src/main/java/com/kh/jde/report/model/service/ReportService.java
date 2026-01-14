package com.kh.jde.report.model.service;

import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportListDTO;

public interface ReportService {
	
	// 댓글 신고 등록
	void createCommentReport(CommentReportCreateDTO dto);
	
	// 리뷰 신고 등록
	void createReviewReport(ReviewReportCreateDTO dto);
	
	// 댓글 신고 페이징 조회
	ReportPageResponse<CommentReportListDTO> getCommentReportList(int currentPage);
	
	// 리뷰 신고 페이징 조회
	ReportPageResponse<ReviewReportListDTO> getReviewReportList(int currentPage);

	// 댓글 신고 상세 조회
	CommentReportListDTO getCommentReportByNo(Long reportNo);
	
	// 리뷰 신고 상세 조회
	ReviewReportListDTO getReviewReportByNo(Long reportNo);
}
