package com.kh.jde.admin.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.jde.admin.model.dto.CommentListDTO;
import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;

import com.kh.jde.admin.model.dto.ReviewListDTO;
import com.kh.jde.admin.model.vo.DefaultImageVO;

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
	
	// 댓글 신고 키워드 검색 전체 개수 조회
	int countCommentReportsByKeyword(String keyword);
	
	// 댓글 신고 키워드 검색 페이징 조회
	List<CommentReportListDTO> selectCommentReportListByKeyword(Map<String, Object> params);
	
	// 리뷰 신고 전체 개수 조회
	int countAllReviewReports();
	
	// 리뷰 신고 페이징 조회
	List<ReviewReportListDTO> selectReviewReportList(PageInfo pageInfo);
	
	// 댓글 신고 상세 조회
	CommentReportListDTO selectCommentReportByNo(Long reportNo);
	
	// 댓글 신고 상세 조회 (처리 후 조회용 - STATUS 조건 없음)
	CommentReportListDTO selectCommentReportByNoAfterProcess(Long reportNo);
	
	// 리뷰 신고 상세 조회
	ReviewReportListDTO selectReviewReportByNo(Long reportNo);
	
	// 리뷰 신고 상세 조회 (처리 후 조회용 - STATUS 조건 없음)
	ReviewReportListDTO selectReviewReportByNoAfterProcess(Long reportNo);
	
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
	
	// 미식대장 랭킹 제한 기준 변경
	int updateCaptainRankPolicy(int topN);

	// 회원 권한 변경
	int updateMemberRole(MemberRoleUpdateDTO dto);

	// 회원 삭제 (STATUS = 'N'으로 변경)
	int deleteMember(Long memberNo);
	
	// 댓글 전체 개수 조회
	int countAllComments();
	
	// 댓글 페이징 조회
	List<CommentListDTO> selectCommentList(PageInfo pageInfo);
	
	// 댓글 상세 조회
	CommentListDTO selectCommentByNo(Long commentNo);
	
	// 댓글 삭제 (STATUS = 'N'으로 변경)
	int deleteComment(Long commentNo);
	
	// 리뷰 페이징 조회
	List<ReviewListDTO> selectReviewList(PageInfo pageInfo);

	// 리뷰 상세 조회
	ReviewListDTO selectReviewByNo(Long reviewNo);

	// 리뷰 삭제 (STATUS = 'N'으로 변경)
	int deleteReview(Long reviewNo);
	
	// 회원 기본 프로플 이미지 등록
	void createDefaultImage(DefaultImageVO defaultImage);
	
}
