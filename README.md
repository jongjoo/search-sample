# Search Sample

---

## Introduce

통합 장소 검색 서비스 입니다.

Kakao API, Naver API를 연동하여 통합 결과를 노출합니다.  

---
## Tech Spec

- Java17
- Spring Boot
- JPA
- Webflux
- Querydsl
- H2
- Redis
- Lombok

---

## APIs

- 통합 장소 검색 기능
  - GET /local/place/keyword/{name}
  - /src/test/java/com/example/searchsample/api/SearchHistory-requests.http


- 검색어 순위 조회 (5초 Cache 적용되어있음.)
  - GET /local/history/rank
  - /src/test/java/com/example/searchsample/api/PlaceSearch-requests.http

---

## 사용법

개발환경에 H2, Redis 실행 후 Application 실행.

---

## 특이사항



