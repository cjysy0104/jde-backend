package com.kh.jde.admin.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jde.admin.model.dao.AdminMapper;
import com.kh.jde.common.page.PageInfo;
import com.kh.jde.common.page.Pagination;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.CommentReportProcessDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportProcessDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final AdminMapper adminMapper;
	private static final int PAGE_LIMIT = 10; // 페이징바에 표시될 페이지 수
	private static final int BOARD_LIMIT = 15; // 한 페이지에 표시될 게시글 수
	
	@Override
	public ReportPageResponse<CommentReportListDTO> getCommentReportList(int currentPage) {
		// 전체 개수 조회
		int listCount = adminMapper.countAllCommentReports();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회
		List<CommentReportListDTO> reportList = adminMapper.selectCommentReportList(pageInfo);
		
		return new ReportPageResponse<>(reportList, pageInfo);
	}
	
	@Override
	public ReportPageResponse<ReviewReportListDTO> getReviewReportList(int currentPage) {
		// 전체 개수 조회
		int listCount = adminMapper.countAllReviewReports();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회
		List<ReviewReportListDTO> reportList = adminMapper.selectReviewReportList(pageInfo);
		
		return new ReportPageResponse<>(reportList, pageInfo);
	}
	
	@Override
	public CommentReportListDTO getCommentReportByNo(Long reportNo) {
		return adminMapper.selectCommentReportByNo(reportNo);
	}
	
	@Override
	public ReviewReportListDTO getReviewReportByNo(Long reportNo) {
		return adminMapper.selectReviewReportByNo(reportNo);
	}
	
	@Override
	@Transactional
	public CommentReportListDTO processCommentReport(CommentReportProcessDTO dto) {
		// 처리 상태 유효성 검증
		validateReportProcess(dto.getReportProcess());
		
		// 신고 처리 업데이트
		int result = adminMapper.updateCommentReportProcess(dto);
		validateUpdateResult(result, "댓글");
		
		// 업데이트된 신고 정보 조회 (처리 시간 포함)
		return adminMapper.selectCommentReportByNo(dto.getReportNo());
	}
	
	@Override
	@Transactional
	public ReviewReportListDTO processReviewReport(ReviewReportProcessDTO dto) {
		// 처리 상태 유효성 검증
		validateReportProcess(dto.getReportProcess());
		
		// 신고 처리 업데이트
		int result = adminMapper.updateReviewReportProcess(dto);
		validateUpdateResult(result, "리뷰");
		
		// 업데이트된 신고 정보 조회 (처리 시간 포함)
		return adminMapper.selectReviewReportByNo(dto.getReportNo());
	}
	
	// 처리 상태 유효성 검증
	private void validateReportProcess(String reportProcess) {
		if (reportProcess == null || reportProcess.isBlank()) {
			throw new IllegalArgumentException("처리 상태는 필수입니다.");
		}
		
		// 유효한 처리 상태인지 확인
		boolean isValid = reportProcess.equals("PENDING") 
				|| reportProcess.equals("IN_PROGRESS")
				|| reportProcess.equals("RESOLVED")
				|| reportProcess.equals("REJECTED");
		
		if (!isValid) {
			throw new IllegalArgumentException("유효하지 않은 처리 상태입니다. (PENDING, IN_PROGRESS, RESOLVED, REJECTED 중 하나여야 합니다.)");
		}
	}
	
	// 업데이트 결과 검증
	private void validateUpdateResult(int result, String reportType) {
		if (result != 1) {
			throw new IllegalStateException(reportType + " 신고 처리에 실패했습니다. 신고 번호를 확인해주세요.");
		}
	}

}
