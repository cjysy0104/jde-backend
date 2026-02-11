내가 만든 기능들 항목

주요 내용

| 항목 | 내용 |
|------|------|
| 프로젝트 명 | JustDoEat (JDE) |
| 수행 기간 | 2025.00.00 ~ 2025.00.00 (수정 필요) |

개발 목표
- JustDoEat 프로젝트는 맛집 정보 공유 및 리뷰 커뮤니티 기능을 제공합니다.
- 신고 기능을 통해 부적절한 리뷰·댓글 관리를 지원합니다.
- 관리자 페이지를 통해 회원·신고·콘텐츠 관리를 수행할 수 있도록 합니다.

---

사용 기술 및 개발 환경

| 구분 | 내용 |
|------|------|
| 운영체제 | Windows 10 |
| 사용언어 | Java, JavaScript, HTML, CSS |
| FrameWork / Library | Spring Boot, React |
| DB | Oracle |
| Tool | STS4, DBeaver, VS Code |
| WAS | Tomcat (Spring Boot Embedded) |
| Collaboration | GitHub, Figma, ERD, Google sheets, Discord, Notion |

---

구현 기능

○ 팀 구현 기능  
● 개인 담당 기능 및 역할  
◑ 공동 기여 부분  

---

○ 헤더
- 로그인 / 로그아웃 & 회원가입
- 로그인 후 마이페이지 진입점
- 로그인 후 각 기능 페이지 진입점

○ 푸터
- 담당자 정보
- 각 페이지로 이동

○ 메인 페이지
- 맛집(레스토랑) 목록 및 리뷰 진입점

○ 마이페이지
- 회원정보 조회 및 수정 (이름, 닉네임, 전화번호, 비밀번호)
- 프로필 이미지 변경 / 기본 이미지 선택
- 회원탈퇴

○ 관리자 페이지 (팀 구현)
- 회원 목록·상세·키워드 검색 / 권한 변경 / 회원 삭제
- 미식대장 랭킹 기준 조회 및 변경
- 댓글·리뷰 목록·상세·키워드 검색 / 삭제
- 기본 프로필 이미지 등록·조회·삭제
- 월별 리뷰 수, 신규 가입자 수, 이용자 전체 수 등 통계 조회
(신고 관련 기능은 아래 ● 신고에서 개인 담당)

○ 리뷰 (Review)
- 리뷰 목록·상세 조회 / 등록 / 수정 / 삭제
- 미식대장(captain) 리뷰 조회 / 베스트 리뷰 / 키워드 검색
- 리뷰 좋아요 / 북마크

○ 댓글 (Comment)
- 리뷰별 댓글 조회 / 작성 / 수정 / 삭제
- 댓글 좋아요

---

● 레스토랑 (Restaurant)
- 레스토랑 목록 조회 (무한 스크롤)
- 레스토랑 검색 (키워드 기반)
- 레스토랑별 리뷰 목록 조회

● API / 백엔드 공통
- 커서 기반 페이지네이션 적용 (cursor, size, size+1 패턴)

● 신고 (Report) - 사용자·관리자 신고 관련 기능 전부 개인 담당
- 댓글/리뷰 신고 등록 (사용자)
- 신고 카테고리 조회
- 내 신고 목록 조회 (사용자)
- 댓글 신고 목록·상세·키워드 검색·처리 (관리자)
- 리뷰 신고 목록·상세·키워드 검색·처리 (관리자)

◑ ERD 작업
◑ 개발환경 구축
◑ 전역 예외 처리기 구현 (GlobalExceptionHandler)

---

프로젝트 참여 소감

(직접 작성하여 채워 넣으시면 됩니다.)

피드백 반영 예시  
- 아쉬웠던 점을 보완하는 부분은 ‘추후에는 ~ 해서 해결하도록 하겠습니다.’ 구조로 작성하면 더 명확합니다.
- 이후 프로젝트 때 이번에 아쉬웠던 점을 어떻게 보완할지, 보완 방향을 한 줄 정도 작성해 두시면 좋습니다.

---

레스토랑 목록·검색 페이지 (개인 담당)

구현 기능 설명

1. 레스토랑 목록 조회 기능 (무한 스크롤)  
1) `GET /api/restaurants`로 전체 레스토랑 목록을 조회합니다.  
2) 쿼리 파라미터 `cursor`, `size`를 받아 커서 기반 무한 스크롤을 구현했습니다.  
   - `RestaurantQueryDTO`에 `ScrollRequest`(size, sizePlusOne)를 포함하고, 서비스에서 cursor·size를 기본값(20)으로 정규화합니다.  
3) MyBatis 동적 SQL `<if test="cursor != null">`로 첫 요청(cursor 없음)과 다음 페이지 요청(cursor 있음)을 하나의 쿼리로 처리합니다.  
4) `FETCH FIRST #{scroll.sizePlusOne} ROWS ONLY`로 size+1건을 조회하여, 클라이언트에서 다음 페이지 존재 여부를 판단할 수 있도록 했습니다.  

2. 레스토랑 검색 기능  
1) `GET /api/restaurants/search`로 키워드 기반 검색을 제공합니다.  
2) `RestaurantSearchDTO`의 `keyword`로 식당명(NORMAL_NAME), 주소(ADDRESS) LIKE 검색을 수행합니다.  
3) MyBatis `#{}` 바인딩을 사용하여 SQL Injection을 방지했습니다.  
4) 목록과 동일하게 커서·size 정규화 및 무한 스크롤을 적용했습니다.  

3. 레스토랑별 리뷰 목록 조회  
1) `GET /api/restaurants/{restaurantNo}/reviews`로 해당 레스토랑의 리뷰 목록을 조회합니다.  
2) `@PathVariable`로 restaurantNo를 받고, `QueryDTO`에 설정한 뒤 기존 `ReviewService.getReviewList`를 재사용합니다.  
3) `@AuthenticationPrincipal CustomUserDetails`로 로그인 여부를 전달하여, 리뷰별 좋아요/북마크 등 개인화 정보를 포함할 수 있도록 했습니다.  

4. 성능 개선 (N+1 방지)  
1) 레스토랑 목록/검색 시 각 행의 대표 썸네일(좋아요가 가장 많은 리뷰의 이미지)을 스칼라 서브쿼리로 한 번에 조회하도록 구현했습니다.  
2) 별도 Mapper 호출 없이 한 번의 SELECT로 목록과 썸네일을 함께 가져와 쿼리 수를 1+N → 1로 줄였습니다.  
   - 상세 내용은 `docs/troubleshooting-restaurant-n+1.md` 참고.  

5. 공통 사항  
1) 조회 API에는 `@Transactional(readOnly = true)`를 적용하여 읽기 전용 트랜잭션으로 처리했습니다.  
2) `@ModelAttribute`로 들어오는 request가 null일 수 있어, 서비스의 normalize 단계에서 null일 경우 빈 DTO로 생성 후 정규화하도록 했습니다.  

---

관리자 페이지 - 신고 처리 (개인 담당)

구현 기능 설명

1. 신고 목록 조회 기능  
1) 댓글 신고 목록 `GET /api/admin/reports/comment`, 리뷰 신고 목록 `GET /api/admin/reports/review`를 페이징 조회합니다.  
2) `page` 파라미터 기본값 1로 처리하고, `ReportPageResponse`로 목록과 페이지 정보를 반환합니다.  

2. 신고 상세 조회 및 처리  
1) 댓글/리뷰 신고 상세 조회 후, 관리자가 처리(승인/반려 등) 상태를 변경할 수 있도록 PUT API를 구현했습니다.  
2) 신고 처리 시 `CommentReportProcessDTO`, `ReviewReportProcessDTO`로 처리 내용을 전달합니다.  

3. 신고 키워드 검색  
1) 댓글/리뷰 신고 목록을 키워드로 검색할 수 있는 API를 제공합니다.  

(위 신고 관련 API·기능 전부 개인 담당으로 구현)

---

트러블 슈팅

- 레스토랑 목록 N+1 쿼리  
  - 원인: 목록 조회 후 각 레스토랑마다 썸네일을 따로 조회하여 1+N번 쿼리 발생.  
  - 해결: MyBatis SELECT 절에 스칼라 서브쿼리로 대표 썸네일을 한 번에 조회.  
  - 자세한 원인 코드·해결 코드는 `docs/troubleshooting-restaurant-n+1.md` 참고.

---

수행 기간, 팀/개인 구분(○ ● ◑), 참여 소감 등은 실제 프로젝트 경험에 맞게 수정하여 사용하시면 됩니다.
