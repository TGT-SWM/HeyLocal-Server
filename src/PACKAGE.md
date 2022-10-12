# 패키지 구조
```bash
│── src
│   ├── main
│   │   ├── java/com/heylocal/traveler
│   │   │   ├── config : Spring 설정 패키지
│   │   │   ├── controller : 컨트롤러 패키지
│   │   │   │   ├── advice : 예외 처리
│   │   │   │   ├── api : 컨트롤러 인터페이스
│   │   │   │   └── resolver : Argument Resolver
│   │   │   ├── domain : 도메인 엔티티 패키지
│   │   │   │   ├── article : 관리자 포스트
│   │   │   │   ├── char : 채팅
│   │   │   │   ├── notification : 알림
│   │   │   │   ├── place : 방문 장소
│   │   │   │   ├── plan : 여행 스케줄표
│   │   │   │   ├── profile : 사용자 프로필
│   │   │   │   ├── token : 인증 토큰
│   │   │   │   ├── travelon : 여행On (게시글)
│   │   │   │   └── user : 사용자
│   │   │   ├── dto : DTO 패키지
│   │   │   │   └── aws : AWS 관련 DTO
│   │   │   ├── exception : 예외
│   │   │   │   └── code : 예외 코드
│   │   │   ├── interceptor : Spring 인터셉터 패키지
│   │   │   │   ├── auth : 인가 인터셉터
│   │   │   │   └── notfound : NotFound 예외 처리 인터셉터
│   │   │   ├── mapper : Entity <-> DTO 변환 패키지
│   │   │   │   └── context : 매핑 컨텍스트 (매핑 로직)
│   │   │   ├── repository : DB 접근 JPA 패키지
│   │   │   ├── service : 서비스 비즈니스 로직 패키지
│   │   │   ├── util : 기타 유틸리티 패키지
│   │   │   │   ├── aws : AWS 접근 유틸
│   │   │   │   ├── date : 날짜 객체 처리 유틸
│   │   │   │   ├── error : 예외 처리 관련 유틸
│   │   │   │   ├── jpa : JPA 관련 유틸
│   │   │   │   └── jwt : JWT 토큰 처리 유틸
│   │   └── resources : 설정 파일, 개발용 SQL 스크립트 관리
└   └── test : 테스트 코드
```