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

* KaKao, Naver 통합 검색 API를 Webflux를 활용하여 논블로킹 방식으로 처리하여 속도 개선.
* 서비스 단위로 기능 분리.
* 검색어 순위 조회를 위해 DB를 사용해 검색시마다 데이터를 row 단위 이력 저장. (동시성에 대한 문제 해결)
* query에 부하가 예견되어 Redis를 활용해 Cache 적용 (대용량 트래픽 처리를 위해 적용)
* 조회(Read)와 등록(Create)에 대한 결합도를 줄이기 위함. 더 나아가 CQRS 패턴도 적용 가능.
* Querydsl 적용하여 가독성 및 개발편의성

