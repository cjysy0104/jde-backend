package com.kh.jde.report.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.vo.CommentReportVO;
import com.kh.jde.report.model.vo.ReportCategoryVO;
import com.kh.jde.report.model.vo.ReviewReportVO;

@Mapper
public interface ReportMapper {
	
	// 댓글 신고 등록
	int insertCommentReport(CommentReportVO commentReport);
	
	// 리뷰 신고 등록
	int insertReviewReport(ReviewReportVO reviewReport);
	
	// 댓글 신고 중복 체크 (동일 사용자가 같은 댓글을 이미 신고했는지)
	int countCommentReportByMemberAndComment(CommentReportCreateDTO commentReport);
	
	// 리뷰 신고 중복 체크 (동일 사용자가 같은 리뷰를 이미 신고했는지)
	int countReviewReportByMemberAndReview(ReviewReportCreateDTO reviewReport);
	
	// 카테고리 목록
	  List<ReportCategoryVO> selectReportCategories();
}
