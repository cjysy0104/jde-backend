package com.kh.jde.admin.model.service;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.jde.admin.model.dao.AdminMapper;
import com.kh.jde.admin.model.dto.CommentListDTO;
import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.SearchDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;
import com.kh.jde.admin.model.vo.DefaultImageVO;
import com.kh.jde.admin.model.dto.ReviewListDTO;
import com.kh.jde.common.page.PageInfo;
import com.kh.jde.common.page.Pagination;
import com.kh.jde.exception.UnexpectedSQLResponseException;
import com.kh.jde.file.FileRenamePolicy;
import com.kh.jde.file.service.S3Service;
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
	private final FileRenamePolicy fileRenamePolicy;
	private final S3Service s3Service;
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
		// 처리 완료/반려 시 STATUS가 'N'으로 변경되므로 STATUS 조건 없는 메서드 사용
		return adminMapper.selectCommentReportByNoAfterProcess(dto.getReportNo());
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
		// 처리 완료/반려 시 STATUS가 'N'으로 변경되므로 STATUS 조건 없는 메서드 사용
		return adminMapper.selectReviewReportByNoAfterProcess(dto.getReportNo());
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
	
	@Override
	public ReportPageResponse<MemberListDTO> getMemberList(int currentPage) {
		// 전체 개수 조회
		int listCount = adminMapper.countAllMembers();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회 (DB에서 마스킹된 데이터 조회)
		List<MemberListDTO> memberList = adminMapper.selectMemberList(pageInfo);
		
		return new ReportPageResponse<>(memberList, pageInfo);
	}
	
	@Override
	public ReportPageResponse<MemberListDTO> getMemberListByKeyword(SearchDTO dto) {
		// 키워드 검색 전체 개수 조회
		int listCount = adminMapper.countMembersByKeyword(dto.getKeyword());
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, dto.getCurrentPage(), PAGE_LIMIT, BOARD_LIMIT);
		
		// Map으로 파라미터 묶기
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", dto.getKeyword());
		params.put("offset", pageInfo.getOffset());
		params.put("boardLimit", pageInfo.getBoardLimit());
		
		// 키워드 검색 페이징 조회
		List<MemberListDTO> memberList = adminMapper.selectMemberListByKeyword(params);
		
		return new ReportPageResponse<>(memberList, pageInfo);
	}
	
	@Override
	public ReportPageResponse<CommentReportListDTO> getCommentReportListByKeyword(SearchDTO dto) {
		// 키워드 검색 전체 개수 조회
		int listCount = adminMapper.countCommentReportsByKeyword(dto.getKeyword());
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, dto.getCurrentPage(), PAGE_LIMIT, BOARD_LIMIT);
		
		// Map으로 파라미터 묶기
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", dto.getKeyword());
		params.put("offset", pageInfo.getOffset());
		params.put("boardLimit", pageInfo.getBoardLimit());
		
		// 키워드 검색 페이징 조회
		List<CommentReportListDTO> reportList = adminMapper.selectCommentReportListByKeyword(params);
		
		return new ReportPageResponse<>(reportList, pageInfo);
	}
	
	@Override
	public ReportPageResponse<ReviewReportListDTO> getReviewReportListByKeyword(SearchDTO dto) {
		// 키워드 검색 전체 개수 조회
		int listCount = adminMapper.countReviewReportsByKeyword(dto.getKeyword());
		
		PageInfo pageInfo = Pagination.getPageInfo(listCount, dto.getCurrentPage(), PAGE_LIMIT, BOARD_LIMIT);
		
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", dto.getKeyword());
		params.put("offset", pageInfo.getOffset());
		params.put("boardLimit", pageInfo.getBoardLimit());
		
		List<ReviewReportListDTO> reportList = adminMapper.selectReviewReportListByKeyword(params);
		
		return new ReportPageResponse<>(reportList, pageInfo);
	}
	
	@Override
	public MemberDetailDTO getMemberByNo(Long memberNo) {
		MemberDetailDTO member = adminMapper.selectMemberByNo(memberNo);
		if (member == null) {
			throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
		}
		return member; // DB에서 이미 마스킹된 데이터를 조회하므로 그대로 반환
	}
	
	@Override
	@Transactional
	public void updateMemberRole(MemberRoleUpdateDTO dto) {
		// 자기 자신의 권한은 수정할 수 없음
		if (dto.getMemberNo().equals(dto.getCurrentMemberNo())) {
			throw new IllegalArgumentException("자기 자신의 권한은 변경할 수 없습니다.");
		}
		
		// 권한 유효성 검증
		validateRole(dto.getRole());
		
		// 회원 권한 변경
		int result = adminMapper.updateMemberRole(dto);
		if (result != 1) {
			throw new IllegalStateException("회원 권한 변경에 실패했습니다. 회원 번호를 확인해주세요.");
		}
	}
	
	// 권한 유효성 검증
	private void validateRole(String role) {
		if (role == null || role.isBlank()) {
			throw new IllegalArgumentException("권한은 필수입니다.");
		}
		
		boolean isValid = role.equals("ROLE_USER") || role.equals("ROLE_ADMIN");
		if (!isValid) {
			throw new IllegalArgumentException("유효하지 않은 권한입니다. (ROLE_USER, ROLE_ADMIN 중 하나여야 합니다.)");
		}
	}
	

	// 미식대장 랭킹 제한 기준 변경(미식대장을 몇 명이나 선정해서 보여줄것인지)
	@Override
	@Transactional
	public void updateCaptainRankPolicy(int topN) {
		
		int result = adminMapper.updateCaptainRankPolicy(topN);
		if(result < 1) {
			throw new UnexpectedSQLResponseException("미식대장 랭킹 기준 변경에 실패했습니다.");
		}
	}
	



	@Override
	@Transactional
	public void deleteMember(Long memberNo) {
		// 회원 삭제 (STATUS를 'N'으로 변경)
		int result = adminMapper.deleteMember(memberNo);
		if (result != 1) {
			throw new IllegalStateException("회원 삭제에 실패했습니다. 회원 번호를 확인해주세요.");
		}
	}
	
	@Override
	public ReportPageResponse<CommentListDTO> getCommentList(int currentPage) {
		// 전체 개수 조회
		int listCount = adminMapper.countAllComments();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회
		List<CommentListDTO> commentList = adminMapper.selectCommentList(pageInfo);
		
		return new ReportPageResponse<>(commentList, pageInfo);
	}
	
	@Override
	public CommentListDTO getCommentByNo(Long commentNo) {
		CommentListDTO comment = adminMapper.selectCommentByNo(commentNo);
		if (comment == null) {
			throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
		}
		return comment;
	}
	
	@Override
	public ReportPageResponse<CommentListDTO> getCommentByKeyword(SearchDTO dto) {
		// 키워드 검색 전체 개수 조회
		int listCount = adminMapper.countCommentsByKeyword(dto.getKeyword());
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, dto.getCurrentPage(), PAGE_LIMIT, BOARD_LIMIT);
		
		// Map으로 파라미터 묶기
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", dto.getKeyword());
		params.put("offset", pageInfo.getOffset());
		params.put("boardLimit", pageInfo.getBoardLimit());
		
		// 키워드 검색 페이징 조회
		List<CommentListDTO> commentList = adminMapper.selectCommentListByKeyword(params);
		
		return new ReportPageResponse<>(commentList, pageInfo);
	}
	
	@Override
	@Transactional
	public void deleteComment(Long commentNo) {
		// 댓글 삭제 (STATUS를 'N'으로 변경)
		int result = adminMapper.deleteComment(commentNo);
		if (result != 1) {
			throw new IllegalStateException("댓글 삭제에 실패했습니다. 댓글 번호를 확인해주세요.");
		}
	}
	
	@Override
	public ReportPageResponse<ReviewListDTO> getReviewList(int currentPage) {

		// 전체 개수 조회
		int listCount = adminMapper.countAllComments();
				
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
				
		// 페이징 조회
		List<ReviewListDTO> reviewList = adminMapper.selectReviewList(pageInfo);
		
		return new ReportPageResponse<>(reviewList, pageInfo);
	}

	@Override
	public ReviewListDTO getReviewByNo(Long reviewNo) {
		ReviewListDTO review = adminMapper.selectReviewByNo(reviewNo);
		if (review == null) {
			throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
		}
		return review;
	}

	@Override
	public ReportPageResponse<ReviewListDTO> getReviewsByKeyword(SearchDTO dto) {
		// 키워드 검색 전체 개수 조회
		int listCount = adminMapper.countReviewsByKeyword(dto.getKeyword());
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, dto.getCurrentPage(), PAGE_LIMIT, BOARD_LIMIT);
		
		// Map으로 파라미터 묶기
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", dto.getKeyword());
		params.put("offset", pageInfo.getOffset());
		params.put("boardLimit", pageInfo.getBoardLimit());
		
		// 키워드 검색 페이징 조회
		List<ReviewListDTO> reviewList = adminMapper.selectReviewListByKeyword(params);
		
		return new ReportPageResponse<>(reviewList, pageInfo);
	}
	
	@Override
	public void deleteReview(Long reviewNo) {
		// 리뷰 삭제 (STATUS를 'N'으로 변경)
		int result = adminMapper.deleteReview(reviewNo);
		if (result != 1) {
			throw new IllegalStateException("댓글 삭제에 실패했습니다. 댓글 번호를 확인해주세요.");
		}
	}

	@Override
	@Transactional
	public void createDefaultImage(String fileName, MultipartFile file) {
		
		fileNameDuplicateCheck(fileName);
		
		String fileUrl = s3Service.fileSave(file, "DefaultImage");
		DefaultImageVO defaultImage = DefaultImageVO.builder()
				.fileName(fileName)
				.fileUrl(fileUrl)
				.build();
		try {
			adminMapper.createDefaultImage(defaultImage);
		} catch(RuntimeException e) {
			throw new UnexpectedSQLResponseException("회원 기본 이미지 등록에 실패했습니다.");
		}
	}
	
	private void fileNameDuplicateCheck(String fileName) {
		DefaultImageVO duplicateCheck = DefaultImageVO.builder()
				.fileName(fileName)
				.build();
		int result = adminMapper.countByFileName(duplicateCheck);
		
		if(result >= 1) {
			throw new UnexpectedSQLResponseException("동일한 이름의 프로필 이미지가 이미 존재합니다.");
		}
	}



}
