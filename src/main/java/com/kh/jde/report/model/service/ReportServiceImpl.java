package com.kh.jde.report.model.service;

import java.sql.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.kh.jde.common.page.PageInfo;
import com.kh.jde.common.page.Pagination;
import com.kh.jde.exception.DuplicateReportException;
import com.kh.jde.report.model.dao.ReportMapper;
import com.kh.jde.report.model.dto.CommentReportCreateDTO;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportCreateDTO;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.vo.CommentReportVO;
import com.kh.jde.report.model.vo.ReviewReportVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	
	private final ReportMapper reportMapper;
	private static final int PAGE_LIMIT = 10; // 페이징바에 표시될 페이지 수
	private static final int BOARD_LIMIT = 15; // 한 페이지에 표시될 게시글 수
	
	@Override
	@Transactional
	public void createCommentReport(CommentReportCreateDTO dto) {
		// 중복 신고 체크
		checkDuplicateCommentReport(dto);
		
		// CommentReportVO 생성 및 등록
		CommentReportVO commentReport = buildCommentReportVO(dto);
		int result = reportMapper.insertCommentReport(commentReport);
		validateInsertResult(result, "댓글");
	}
	
	@Override
	@Transactional
	public void createReviewReport(ReviewReportCreateDTO dto) {
		// 중복 신고 체크
		checkDuplicateReviewReport(dto);
		
		// ReviewReportVO 생성 및 등록
		ReviewReportVO reviewReport = buildReviewReportVO(dto);
		int result = reportMapper.insertReviewReport(reviewReport);
		validateInsertResult(result, "리뷰");
	}
	
	// 댓글 신고 중복 체크
	private void checkDuplicateCommentReport(CommentReportCreateDTO dto) {
		int count = reportMapper.countCommentReportByMemberAndComment(dto);
		if (count > 0) {
			throw new DuplicateReportException("이미 신고한 댓글입니다.");
		}
	}
	
	// 리뷰 신고 중복 체크
	private void checkDuplicateReviewReport(ReviewReportCreateDTO dto) {
		int count = reportMapper.countReviewReportByMemberAndReview(dto);
		if (count > 0) {
			throw new DuplicateReportException("이미 신고한 리뷰입니다.");
		}
	}
	
	// 현재 시간 반환
	private Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	
	// CommentReportVO 생성
	private CommentReportVO buildCommentReportVO(CommentReportCreateDTO dto) {
		Date currentDate = getCurrentDate();
		return CommentReportVO.builder()
				.memberNo(dto.getMemberNo())
				.commentNo(dto.getCommentNo())
				.reportCategoryNo(dto.getReportCategoryNo())
				.reportContent(dto.getReportContent())
				.reportProcess("PENDING")
				.createdAt(currentDate)
				.updateAt(currentDate)
				.status("Y")
				.build();
	}
	
	// ReviewReportVO 생성
	private ReviewReportVO buildReviewReportVO(ReviewReportCreateDTO dto) {
		Date currentDate = getCurrentDate();
		return ReviewReportVO.builder()
				.memberNo(dto.getMemberNo())
				.reviewNo(dto.getReviewNo())
				.reportCategoryNo(dto.getReportCategoryNo())
				.reportContent(dto.getReportContent())
				.reportProcess("PENDING")
				.createdAt(currentDate)
				.updateAt(currentDate)
				.status("Y")
				.build();
	}
	
	//신고 등록 결과 검증
	private void validateInsertResult(int result, String reportType) {
		if (result != 1) {
			throw new IllegalStateException(reportType + " 신고 등록에 실패했습니다.");
		}
	}
	
	@Override
	public ReportPageResponse<CommentReportListDTO> getCommentReportList(int currentPage) {
		// 전체 개수 조회
		int listCount = reportMapper.countAllCommentReports();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회
		List<CommentReportListDTO> reportList = reportMapper.selectCommentReportList(pageInfo);
		
		return new ReportPageResponse<>(reportList, pageInfo);
	}
	
	@Override
	public ReportPageResponse<ReviewReportListDTO> getReviewReportList(int currentPage) {
		// 전체 개수 조회
		int listCount = reportMapper.countAllReviewReports();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회
		List<ReviewReportListDTO> reportList = reportMapper.selectReviewReportList(pageInfo);
		
		return new ReportPageResponse<>(reportList, pageInfo);
	}
}
