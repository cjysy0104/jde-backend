package com.kh.jde.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.jde.admin.model.dto.CommentListDTO;
import com.kh.jde.admin.model.dto.MemberDetailDTO;
import com.kh.jde.admin.model.dto.MemberListDTO;
import com.kh.jde.admin.model.dto.MemberRoleUpdateDTO;
import com.kh.jde.admin.model.service.AdminService;
import com.kh.jde.auth.model.vo.CustomUserDetails;
import com.kh.jde.common.responseData.SuccessResponse;
import com.kh.jde.report.model.dto.CommentReportListDTO;
import com.kh.jde.report.model.dto.CommentReportProcessDTO;
import com.kh.jde.report.model.dto.ReportPageResponse;
import com.kh.jde.report.model.dto.ReviewReportListDTO;
import com.kh.jde.report.model.dto.ReviewReportProcessDTO;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	// 댓글 신고 페이징 조회
	@GetMapping("/reports/comment")
	public ResponseEntity<SuccessResponse<ReportPageResponse<CommentReportListDTO>>> getCommentReportList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<CommentReportListDTO> reportPageResponse = adminService.getCommentReportList(page);
		
		return SuccessResponse.ok(reportPageResponse, "댓글 신고 목록 조회 성공");
	}
	
	// 리뷰 신고 페이징 조회
	@GetMapping("/reports/review")
	public ResponseEntity<SuccessResponse<ReportPageResponse<ReviewReportListDTO>>> getReviewReportList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<ReviewReportListDTO> reportPageResponse = adminService.getReviewReportList(page);
		
		return SuccessResponse.ok(reportPageResponse, "리뷰 신고 목록 조회 성공");
	}
	
	// 댓글 신고 상세 조회
	@GetMapping("/reports/comment/{reportNo}")
	public ResponseEntity<SuccessResponse<CommentReportListDTO>> getCommentReportByNo(
			@PathVariable(name="reportNo") Long reportNo){
		
		CommentReportListDTO commentReport = adminService.getCommentReportByNo(reportNo);
		
		return SuccessResponse.ok(commentReport, "댓글 신고 상세 조회 성공");
	}
	
	// 리뷰 신고 상세 조회
	@GetMapping("/reports/review/{reportNo}")
	public ResponseEntity<SuccessResponse<ReviewReportListDTO>> getReviewReportByNo(
			@PathVariable(name="reportNo") Long reportNo){
		
		ReviewReportListDTO reviewReport = adminService.getReviewReportByNo(reportNo);
		
		return SuccessResponse.ok(reviewReport, "리뷰 신고 상세 조회 성공");
	}
	
	// 댓글 신고 처리
	@PutMapping("/reports/comment/{reportNo}")
	public ResponseEntity<SuccessResponse<CommentReportListDTO>> processCommentReport(
			@PathVariable(name="reportNo") Long reportNo,
			@RequestBody CommentReportProcessDTO dto){
		
		dto.setReportNo(reportNo);
		CommentReportListDTO updatedReport = adminService.processCommentReport(dto);
		
		return SuccessResponse.ok(updatedReport, "댓글 신고가 처리되었습니다.");
	}
	
	// 리뷰 신고 처리
	@PutMapping("/reports/review/{reportNo}")
	public ResponseEntity<SuccessResponse<ReviewReportListDTO>> processReviewReport(
			@PathVariable(name="reportNo") Long reportNo,
			@RequestBody ReviewReportProcessDTO dto){
		
		dto.setReportNo(reportNo);
		ReviewReportListDTO updatedReport = adminService.processReviewReport(dto);
		
		return SuccessResponse.ok(updatedReport, "리뷰 신고가 처리되었습니다.");
	}
	
	// 회원 페이징 조회
	@GetMapping("/members")
	public ResponseEntity<SuccessResponse<ReportPageResponse<MemberListDTO>>> getMemberList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<MemberListDTO> memberPageResponse = adminService.getMemberList(page);
		
		return SuccessResponse.ok(memberPageResponse, "회원 목록 조회 성공");
	}
	
	// 회원 상세 조회 (비밀번호 제외, 개인정보 마스킹)
	@GetMapping("/members/{memberNo}")
	public ResponseEntity<SuccessResponse<MemberDetailDTO>> getMemberByNo(
			@PathVariable(name="memberNo") Long memberNo){
		
		MemberDetailDTO member = adminService.getMemberByNo(memberNo);
		
		return SuccessResponse.ok(member, "회원 상세 조회 성공");
	}
	

	// 미식대장 랭킹 몇위까지 보여줄 것인지 변경 가능	
	@PatchMapping("/captainRank/{topN:\\d+}")
	public ResponseEntity<SuccessResponse<String>> updateCaptainRankPolicy(@PathVariable(name="topN") @Min(value=0, message="0이상의 정수만 입력해주세요.") int topN){
		adminService.updateCaptainRankPolicy(topN);
		
		return SuccessResponse.ok("미식대장 랭킹 기준을 변경했습니다.");
	}
	
	
	// 회원 권한 변경
	@PutMapping("/members/{memberNo}/role")
	public ResponseEntity<SuccessResponse<String>> updateMemberRole(
			@PathVariable(name="memberNo") Long memberNo,
			@RequestBody MemberRoleUpdateDTO dto,
			@AuthenticationPrincipal CustomUserDetails user){
		
		dto.setMemberNo(memberNo);
		dto.setCurrentMemberNo(user.getMemberNo());
		
		adminService.updateMemberRole(dto);
		
		return SuccessResponse.ok("회원 권한이 변경되었습니다.");
	}
	
	// 회원 삭제
	@DeleteMapping("/members/{memberNo}")
	public ResponseEntity<SuccessResponse<String>> deleteMember(
			@PathVariable(name="memberNo") Long memberNo){
		
		adminService.deleteMember(memberNo);
		
		return SuccessResponse.ok("회원이 삭제 되었습니다.");
	}
	
	// 댓글 페이징 조회
	@GetMapping("/comments")
	public ResponseEntity<SuccessResponse<ReportPageResponse<CommentListDTO>>> getCommentList(
			@RequestParam(name = "page", defaultValue = "1") int page) {
		
		ReportPageResponse<CommentListDTO> commentPageResponse = adminService.getCommentList(page);
		
		return SuccessResponse.ok(commentPageResponse, "댓글 목록 조회 성공");
	}
	
	// 댓글 상세 조회
	@GetMapping("/comments/{commentNo}")
	public ResponseEntity<SuccessResponse<CommentListDTO>> getCommentByNo(
			@PathVariable(name="commentNo") Long commentNo){
		
		CommentListDTO comment = adminService.getCommentByNo(commentNo);
		
		return SuccessResponse.ok(comment, "댓글 상세 조회 성공");
	}
	
	// 디폴트 프로필 이미지 등록하기
	@PostMapping("/defaultImage")
	public ResponseEntity<SuccessResponse<String>> createDefaultImage(){
	
		return SuccessResponse.created("회원 기본 이미지 등록에 성공했습니다.");
	}
}






