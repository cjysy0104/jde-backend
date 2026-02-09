> # 프로젝트명 : JDE ( Just Do Eat,  맛집 소개 및 리뷰 서비스)

### 한 줄 소개
* 저희 서비스는 정보 과잉 시대에 광고성 리뷰를 지양하고, 미식가들의 상호 검증(좋아요/미식대장)을 통해 신뢰할 수 있는 맛집 데이터만을 지도 위에 큐레이션하는 [미식 가이드 플랫폼]입니다.

> ## 메인 화면

<img width="1174" height="916" alt="메인1" src="https://github.com/user-attachments/assets/e9873783-36f4-481d-9324-d457c6fbfc04" />

> ## 프로젝트 소개

&nbsp; 이 프로젝트는 Spring Boot 4 + MyBatis + Oracle + JWT 인증 구조의 백엔드 서버로,<br />
다음과 같이 서비스의 핵심 비즈니스 로직과 데이터 처리를 담당합니다.
* JWT 기반 인증/인가 (AccessToken / RefreshToken)
* 회원 관리 (가입/로그인/정보수정/탈퇴, 권한 관리)
* 리뷰/댓글 CRUD 및 검색·정렬·페이징
* 리뷰/댓글 좋아요, 북마크 기능
* 신고 처리 및 관리자(Admin) 전용 API 제공
* AWS S3 연동 파일 업로드/삭제 및 URL DB 저장
* RESTful API 구조 준수 및 예외 처리/응답 포맷 공통화

> ## 개발 기간

* 2026.01.12 ~ 2026.02.11 (약 4주)
* 개발 인원 총 4명

> ## 주요 기능

* ### 회원 관리 ###
  * 회원가입 / 로그인 (JWT 발급)
  * AccessToken & RefreshToken 구조
  * 사용자 권한(ROLE_USER / ROLE_ADMIN)
  * 프로필 정보 수정 및 탈퇴
  * 기본 프로필 이미지 선택 / 프로필 이미지 업로드

* ### 리뷰 조회 ###
  * 리뷰 CRUD
  * 키워드 기반 리뷰 태깅
  * 이미지 파일 업로드(S3) 및 조회
  * 리뷰 좋아요 / 북마크
  * 베스트 리뷰(좋아요 기반) 조회
  * 미식대장(캡틴) 리뷰 목록 제공

* ### 댓글 ###
  * 댓글 CRUD
  * 댓글 좋아요
  * 페이징 처리

* ### 지도 기반 탐색 ###
  * 지도 위 맛집 탐색 및 좌표 기반 리뷰 큐레이션
  * 마커 클릭 → 해당 위치/가게 리뷰 목록 조회
 
*  ### 신고 ###
   * 리뷰/댓글 신고
   * 신고 카테고리 제공

* ### 관리자(Admin) ###
  * 회원 관리(조회/검색/권한 변경/삭제)
  * 리뷰/댓글 관리(조회/검색/삭제)
  * 신고 관리(목록/상세/처리)
  * 기본 프로필 이미지 관리(등록/조회/삭제)
  * 통계(월별 리뷰 수, 가입자 지표 등)
 
> ## 기술 스택
### Backend
* Java 21
* Spring Boot 3.5.x
* Spring Security + JWT
* MyBatis
* Oracle Database
* Lombok

### Infra / DevOps
* AWS S3 (파일 저장)
* Maven build

> ## 프로젝트 구조

```
src
└── main
    ├── java
    │   └── com.kh.jde
    │       ├── auth
    │       ├── member
    │       ├── review
    │       ├── comment
    │       ├── bookmark
    │       ├── like
    │       ├── report
    │       ├── admin
    │       ├── file
    │       ├── common
    │       │   ├── configuration
    │       │   ├── exception
    │       │   └── util
    ├── resources
    │   ├── mapper
    │   └── application.yml

```


> ## 주요 트러블 슈팅

#### 최준영
1. 내용
   * 내용

> ## 배운점

> ### 최준영
* 내용


> ## 개선 사항

> ### 최준영
* 내용



> ## 팀원 정보


| 이름 | 담당 | GitHub |
|------|------------------------|------------------------------|
| 안준영 (팀장) | 회원관리 기능 | [![GitHub](https://img.shields.io/badge/GitHub-000?logo=github&logoColor=white)](https://github.com/Ahnjunyoung927) &nbsp; (https://github.com/Ahnjunyoung927) |
| 선승제 | 관리자 기능 및 지도 기반 탐색 | [![GitHub](https://img.shields.io/badge/GitHub-000?logo=github&logoColor=white)](https://github.com/seung-2001) &nbsp; (https://github.com/seung-2001)|
| 유성현 | 좋아요/북마크 기능 및 회원관리 기능  | [![GitHub](https://img.shields.io/badge/GitHub-000?logo=github&logoColor=white)](https://github.com/yoosh0610) &nbsp; (https://github.com/yoosh0610)|
| 최준영 | 리뷰 조회 | [![GitHub](https://img.shields.io/badge/GitHub-000?logo=github&logoColor=white)](https://github.com/cjysy0104) &nbsp; (https://github.com/cjysy0104)|


> ## 문의
* Email

| 이름 | 이메일 |
|--------|---------------------|
| 안준영 | younge64@naver.com, younge6400@gmail.com |
| 선승제 | ssj01022734558@gmail.com |
| 유성현 | yoo31318@gmail.com |
| 최준영 | cjysy0104@gmail.com |
* GitHub Issues 활용
