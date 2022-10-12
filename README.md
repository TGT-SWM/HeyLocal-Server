# 현지야 - 내 손안의 여행 길잡이
## 프로젝트 설명
여행 스케줄 작성에 어려움을 겪는 사람들을 위한 여행 솔루션.  

### 여행 목적에 맞는 장소 공유
사용자가 본인이 원하는 여행에 대한 설명을 게시하고, 해당 설명을 기반으로 서비스 사용자들로부터 방문할 장소를 추천받을 수 있음.  
이렇게 추천받은 장소를 내 여행 스케줄표에 추가할 수 있음.  
또한, 직접 다른 사용자들에게 장소를 추천해줄 수 있음.

### 여행 스케줄 관리
사용자가 자신의 여행 스케줄을 쉽게 관리할 수 있는 여행 스케줄표 기능을 제공함.  
사용자가 방문할 장소들을 리스트 형식으로 관리함.  
현재 여행 중인 경우, 사용자의 현재 위치 정보를 기반으로 스케줄표를 실시간 업데이트하여 '장소 도착 예정 정보', '길찾기 정보' 등을 제공함.

<br/>

## 디렉토리 구조
```bash
├── .github
│   └── workflows : Github Action 관련 스크립트
├── doc : 기타 산출물 (발표 자료 등)
└── gradle
│   └── wrapper : 빌드에 필요한 Gradle 래퍼 파일
└── src
│   ├── main : 메인 소스코드
└   └── test : 테스트 소스코드
``` 
패키지 구조는 아래 md 파일 참고  
[PACKAGE.md](https://github.com/TGT-SWM/HeyLocal-Server/blob/main/src/PACKAGE.md)

<br/>

## 환경
### 기술 스택
<span><img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></span>
<span><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"></span>
<span><img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white"></span>
<span><img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=&logoColor=white"></span>

### 개발 환경
- 개발 운영체제 : macOS Monterey 12.6
- JDK 11

### 배포 환경
- 서버 운영체제 : Amazon Linux 2
- AWS Services
  - EC2
  - S3
  - RDS
  - SNS (Simple Notification Service)
- DB
  - MariaDB 10.6.8

### 사용 라이브러리
- `spring-boot-starter-web 2.6.10`
- `spring-boot-starter-data-jpa 2.6.10`
- `spring-boot-starter-thymeleaf 2.6.10`
- `spring-boot-starter-validation 2.6.10`
- `spring-boot-starter-security 2.6.10`
- `springfox-boot-starter 3.0.0`
- `jjwt 0.9.1.Final`
- `mapstruct 1.5.2`
- `spring-cloud-starter-aws 2.2.6.RELEASE`
- `spring-cloud-aws-context 2.2.6.RELEASE`
- `spring-cloud-aws-autoconfigure 2.2.6.RELEASE`

<br/>

## 프로젝트 결과물
### API
|Method|EndPoint|설명|
|------|--------|---|
|------|회원가입|------|
|POST|`/signup`|회원가입|
|GET|`/signup/accountid`|아이디 중복 확인|
|------|로그인|------|
|POST|`/signin`|로그인|
|------|인증 토큰|------|
|PUT|`/auth/access-token`|Token 재발급|
|------|사용자|------|
|GET|`/users/{userId}/opinions`|특정 사용자가 작성한 답변 조회|
|GET|`/users/{userId}/profile`|특정 사용자의 프로필 조회|
|PUT|`/users/{userId}/profile`|특정 사용자의 프로필 수정|
|GET|`/users/{userId}/travel-ons`|작성한 여행On(게시글) 조회|
|GET|`/users/ranking`|랭킹 목록 조회|
|------|여행On (게시글)|------|
|GET|`/travel-ons`|전체 여행On(게시글) 조회|
|POST|`/travel-ons`|여행On 등록|
|GET|`/travel-ons/{travelOnId}`|여행On 상세 조회|
|PUT|`/travel-ons/{travelOnId}`|여행On 수정|
|DELETE|`/travel-ons/{travelOnId}`|여행On 삭제|
|GET|`/travel-ons/{travelOnId}/opinions`|답변 조회|
|POST|`/travel-ons/{travelOnId}/opinions`|답변 등록|
|PUT|`/travel-ons/{travelOnId}/opinions/{opinionId}`|답변 수정|
|DELETE|`/travel-ons/{travelOnId}/opinions/{opinionId}`|답변 삭제|
|------|스케줄|------|
|GET|`/plans`|로그인한 사용자가 작성한 플랜 조회|
|POST|`/plans`|플랜 등록|
|PUT|`/plans/{planId}`|플랜 수정|
|DELETE|`/plans/{planId}`|플랜 삭제|
|GET|`/plans/{planId}/places`|플랜의 장소 목록 조회|
|PUT|`/plans/{planId}/places`|플랜의 장소 목록 수정|
|------|장소|------|
|GET|`/places/{placeId}`|장소 정보 조회|
|GET|`/places/{placeId}/opinions`|특정 장소에 대한 답변 조회|
|GET|`/places/hot`|답변으로 많이 선택된 인기 장소 조회|
|------|지역|------|
|GET|`/regions`|지역 목록 조회|
|------|채팅|------|
|GET|`/chatrooms`|로그인한 사용자의 채팅방 리스트 조회|
|GET|`/chatrooms/{chatroomId}/messages`|특정 채팅방의 메시지 조회|
|------|AWS 관련|------|
|POST|`/aws/sns/opinion/img/delete`|답변 이미지 파일이 S3에서 제거되었을 때, AWS가 호출하는 Callback API|
|POST|`/aws/sns/opinion/img/put`|답변 이미지 파일이 S3에 저장되었을 때, AWS가 호출하는 Callback API|
|POST|`/aws/sns/profile/img/delete`|사용자 프로필 이미지 파일이 S3에서 제거되었을 때, AWS가 호출하는 Callback API|
|POST|`/aws/sns/profile/img/put`|사용자 프로필 이미지 파일이 S3에 저장되었을 때, AWS가 호출하는 Callback API|



## 팀
### 기여
|이름|구현|
|---|-------|
|우태균|- Entity·DB 설계 <br/> - 회원가입·로그인 <br/> - 인가 로직 <br/> - 여행On API <br/> - 장소 API <br/> - 지역 API <br/> - AWS 관련 API <br/> - Infra 설계 및 구현 <br/> - CI/CD 환경 구현|
|신우진|- API 설계 <br/> - 스케줄 API|

### 팀원
|   |이름|역할|GitHub|Blog|
|---|---|---|-----|-----|
|<img src="https://avatars.githubusercontent.com/u/76861101?v=4" alt="drawing" width="100"/>|우태균|TL/Server|[GitHub 프로필](https://github.com/TaegyunWoo)|[개발 블로그](https://taegyunwoo.github.io/)|
|<img src="https://avatars.githubusercontent.com/u/76616101?v=4" alt="drawing" width="100"/>|신우진|Server/Client|[GitHub 프로필](https://github.com/gintooooonic)|[개발 블로그](https://woodyshin.com/)|
|<img src="https://avatars.githubusercontent.com/u/37467592?v=4" alt="drawing" width="100"/>|최정인|Client|[GitHub 프로필](https://github.com/choijungp)|[개발 블로그](https://velog.io/@choijungp)|

<br/><br/>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<br/>

최종 수정일 : 2022-10-06