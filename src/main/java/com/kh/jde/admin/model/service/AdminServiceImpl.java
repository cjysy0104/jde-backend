package com.kh.jde.admin.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.kh.jde.admin.model.dao.AdminMapper;
import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;
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
	
	@Override
	public ReportPageResponse<MemberListDTO> getMemberList(int currentPage) {
		// 전체 개수 조회
		int listCount = adminMapper.countAllMembers();
		
		// PageInfo 생성
		PageInfo pageInfo = Pagination.getPageInfo(listCount, currentPage, PAGE_LIMIT, BOARD_LIMIT);
		
		// 페이징 조회
		List<MemberListDTO> memberList = adminMapper.selectMemberList(pageInfo);
		
		// 개인정보 마스킹 적용
		List<MemberListDTO> maskedList = memberList.stream()
				.map(this::maskMemberList)
				.collect(Collectors.toList());
		
		return new ReportPageResponse<>(maskedList, pageInfo);
	}
	
	
	// 회원 목록용 마스킹
	private MemberListDTO maskMemberList(MemberListDTO member) {
		MemberListDTO masked = new MemberListDTO();
		masked.setMemberNo(member.getMemberNo());
		masked.setEmail(maskEmail(member.getEmail()));
		masked.setMemberName(maskName(member.getMemberName()));
		masked.setNickname(member.getNickname());
		masked.setPhone(maskPhone(member.getPhone()));
		masked.setEnrollDate(member.getEnrollDate());
		masked.setStatus(member.getStatus());
		masked.setRole(member.getRole());
		return masked;
	}
	
	// 회원 상세용 마스킹
	private MemberDetailDTO maskMemberDetail(MemberDetailDTO member) {
		MemberDetailDTO masked = new MemberDetailDTO();
		masked.setMemberNo(member.getMemberNo());
		masked.setEmail(maskEmail(member.getEmail()));
		masked.setMemberName(maskName(member.getMemberName()));
		masked.setNickname(member.getNickname());
		masked.setPhone(maskPhone(member.getPhone()));
		masked.setEnrollDate(member.getEnrollDate());
		masked.setStatus(member.getStatus());
		masked.setRole(member.getRole());
		return masked;
	}
	
	// 이름 마스킹: 가운데 1글자 마스킹 (예: 홍*동)
	private String maskName(String name) {
		if (name == null || name.length() <= 1) {
			return name;
		}
		
		if (name.length() == 2) {
			return name.charAt(0) + "*";
		}
		
		int midIndex = name.length() / 2;
		return name.substring(0, midIndex) + "*" + name.substring(midIndex + 1);
	}
	
	// 이메일 마스킹: 앞 2글자 + 뒤 2글자 노출 (예: yo****64@naver.com)
	private String maskEmail(String email) {
		if (email == null || !email.contains("@")) {
			return email;
		}
		
		String[] parts = email.split("@");
		String localPart = parts[0];
		String domain = parts[1];
		
		if (localPart.length() <= 4) {
			return email; // 너무 짧으면 마스킹하지 않음
		}
		
		String maskedLocal = localPart.substring(0, 2) + "****" + localPart.substring(localPart.length() - 2);
		return maskedLocal + "@" + domain;
	}
	
	// 전화번호 마스킹: 가운데 4자리 마스킹 (예: 010-****-5678)
	private String maskPhone(String phone) {
		if (phone == null || !phone.contains("-")) {
			return phone;
		}
		
		String[] parts = phone.split("-");
		if (parts.length != 3) {
			return phone;
		}
		
		return parts[0] + "-****-" + parts[2];
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

}
