package com.kh.jde.admin.model.service;

import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;
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
	
	// 회원 페이징 조회
	ReportPageResponse<MemberListDTO> getMemberList(int currentPage);
	
	// 회원 상세 조회 (비밀번호 제외, 개인정보 마스킹)
	MemberDetailDTO getMemberByNo(Long memberNo);

	// 회원 삭제 (STATUS를 'N'으로 변경)
	void deleteMember(Long memberNo);
	
}
