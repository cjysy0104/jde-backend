package com.kh.jde.report.model.service;

import java.util.List;

import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.vo.ReportCategoryVO;

public interface ReportService {
	
	// 댓글 신고 등록
	void createCommentReport(CommentReportCreateDTO dto);
	
	// 리뷰 신고 등록
	void createReviewReport(ReviewReportCreateDTO dto);
	
	// 신고 카테고리
	List<ReportCategoryVO> getReportCategories();
}
