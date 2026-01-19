package com.kh.jde.admin.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.admin.model.dto.CommentListDTO;
import com.kh.jde.admin.model.dto.DefaultImageDTO;
import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;
import com.kh.jde.admin.model.dto.ReviewListDTO;
import com.kh.jde.admin.model.dto.SearchDTO;
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
	
	// 댓글 신고 키워드 조회
	ReportPageResponse<CommentReportListDTO> getCommentReportListByKeyword(SearchDTO dto);
	
	// 리뷰 신고 키워드 조회
	ReportPageResponse<ReviewReportListDTO> getReviewReportListByKeyword(SearchDTO dto);
	
	// 회원 페이징 조회
	ReportPageResponse<MemberListDTO> getMemberList(int currentPage);
	
	// 회원 키워드 조회
	ReportPageResponse<MemberListDTO> getMemberListByKeyword(SearchDTO dto);
	
	// 회원 상세 조회 (비밀번호 제외, 개인정보 마스킹)
	MemberDetailDTO getMemberByNo(Long memberNo);

	// 미식대장 랭킹 제한 기준 변경
	void updateCaptainRankPolicy(int topN);

	// 회원 권한 변경
	void updateMemberRole(MemberRoleUpdateDTO dto);

	// 회원 삭제 (STATUS를 'N'으로 변경)
	void deleteMember(Long memberNo);
	
	// 댓글 페이징 조회
	ReportPageResponse<CommentListDTO> getCommentList(int currentPage);
	
	// 댓글 상세 조회
	CommentListDTO getCommentByNo(Long commentNo);
	
	// 댓글 키워드 조회
	ReportPageResponse<CommentListDTO> getCommentByKeyword(SearchDTO dto);
	
	// 댓글 삭제 (STATUS를 'N'으로 변경)
	void deleteComment(Long commentNo);

	// 리뷰 페이징 조회
	ReportPageResponse<ReviewListDTO> getReviewList(int currentPage);

	// 리뷰 상세 조회
	ReviewListDTO getReviewByNo(Long reviewNo);

	// 리뷰 삭제 (STATUS를 'N'으로 변경)
	void deleteReview(Long reviewNo);

	// 회원이 사용할 기본이미지 등록
	void createDefaultImage(String fileName, MultipartFile file);
	
	// 회원이 사용할 기본이미지 조회
	List<DefaultImageDTO> getDefaultImage();
	
}
