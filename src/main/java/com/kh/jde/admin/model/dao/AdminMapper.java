package com.kh.jde.admin.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;
import com.kh.jde.common.page.PageInfo;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.CommentReportProcessDTO;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportProcessDTO;

@Mapper
public interface AdminMapper {
	
	// 댓글 신고 전체 개수 조회
	int countAllCommentReports();
	
	// 댓글 신고 페이징 조회
	List<CommentReportListDTO> selectCommentReportList(PageInfo pageInfo);
	
	// 리뷰 신고 전체 개수 조회
	int countAllReviewReports();
	
	// 리뷰 신고 페이징 조회
	List<ReviewReportListDTO> selectReviewReportList(PageInfo pageInfo);
	
	// 댓글 신고 상세 조회
	CommentReportListDTO selectCommentReportByNo(Long reportNo);
	
	// 리뷰 신고 상세 조회
	ReviewReportListDTO selectReviewReportByNo(Long reportNo);
	
	// 댓글 신고 처리
	int updateCommentReportProcess(CommentReportProcessDTO dto);
	
	// 리뷰 신고 처리
	int updateReviewReportProcess(ReviewReportProcessDTO dto);
	
	// 회원 전체 개수 조회
	int countAllMembers();
	
	// 회원 페이징 조회
	List<MemberListDTO> selectMemberList(PageInfo pageInfo);
	
	// 회원 상세 조회 (비밀번호 제외, 개인정보 마스킹)
	MemberDetailDTO selectMemberByNo(Long memberNo);
	
	// 회원 권한 변경
	int updateMemberRole(MemberRoleUpdateDTO dto);

	// 회원 삭제 (STATUS = 'N'으로 변경)
	int deleteMember(Long memberNo);
	
}
