package com.kh.jde.report.model.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import com.kh.jde.common.page.PageInfo;
import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.vo.CommentReportVO;
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
	
	// 댓글 신고 전체 개수 조회
	int countAllCommentReports();
	
	// 댓글 신고 페이징 조회
	List<CommentReportListDTO> selectCommentReportList(PageInfo pageInfo);
	
	// 리뷰 신고 전체 개수 조회
	int countAllReviewReports();
	
	// 리뷰 신고 페이징 조회
	List<ReviewReportListDTO> selectReviewReportList(PageInfo pageInfo);
}
