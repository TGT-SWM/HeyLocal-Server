# 저장소 개요
- 본 저장소는 SW Maestro 13기 과정에서 진행하는 '현지야 서비스'의 서버 Application을 관리하는 저장소이다.

## 팀 정보
- SW Maestro 13기 TGT팀  

| 이름  | 역할  |업무|
|-----|-----|---|
| 우태균 | 팀장  |BE|
| 신우진 | 팀원  |FE/BE|
| 최정인 | 팀원  |FE|

## TODO
- 답변 수정 테스트코드
- Opinion 엔티티의 Region, Place 필드를 Lazy 로딩으로 설정하고, 조회할 때 fetch join을 하도록 수정
- `Entity` <-> `DTO` Mapper 적용
  - https://jforj.tistory.com/93
- Repository 테스트 코드의 엔티티 비교 검증 로직 중, 모든 assertEqual 을 assertSame 으로 변경
- 답변 항목 DB 반영
- 답변 이미지 처리