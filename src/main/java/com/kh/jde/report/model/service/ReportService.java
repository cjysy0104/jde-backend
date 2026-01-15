package com.kh.jde.report.model.service;

import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;

public interface ReportService {
	
	// 댓글 신고 등록
	void createCommentReport(CommentReportCreateDTO dto);
	
	// 리뷰 신고 등록
	void createReviewReport(ReviewReportCreateDTO dto);
}
