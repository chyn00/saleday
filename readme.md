# SaleDay

사용자가 몰리는 상황에서도 **주문과 재고 데이터가 어긋나지 않도록 설계한 커머스 백엔드 프로젝트**입니다.  
핵심 목표는 **데이터 정확성(주문/재고)** 과 **빠른 처리 속도**를 함께 달성하는 것입니다.

**기간:** 2025.03 ~ (지속적 설계·검증 및 고도화)

---

## 시작 전 안내

- 일부 설정은 `local` 프로파일에 의존하므로 바로 실행이 어려울 수 있습니다.
- Kafka / Redis / MySQL 인프라가 준비되지 않으면 일부 기능은 동작하지 않습니다.

---

## 실행 방법 (Docker Compose)

프로젝트 루트에서 실행:

```bash
docker compose up -d --build
```

확인:

```bash
docker ps
```

기본 포트:

- API: `http://localhost:9999`
- Consumer: `localhost:9998`
- Kafka: `localhost:9092`
- Redis: `localhost:6379`
- MySQL: `localhost:3306`

중지:

```bash
docker compose down
```

---

# 핵심 설계 관점

## 1️⃣ 재고 하한 제어

재고는 어떤 상황에서도 0 이하로 내려가지 않도록 설계했습니다.

- Redis `DECR`로 재고 선차감
- 감소 결과 < 0 → 즉시 보정 후 실패 처리
- DB 반영은 이후 단계에서 수행

Redis는 영속 계층이 아닌 **빠른 판단 계층**으로 한정해 사용했습니다.

---

## 2️⃣ 동기 / 비동기 경계 설정

모든 처리를 동기 트랜잭션으로 묶을 경우, 고트래픽 상황에서 DB 경합이 급격히 증가할 수 있습니다.

따라서:

- **동기 구간:** 재고 감소 가능 여부 판단
- **비동기 구간:** 주문 커밋 이후 DB 재고 반영

으로 경계를 분리했습니다.

---

## 3️⃣ Outbox + 재처리 구조

비동기 구조에서 가장 중요하게 고려한 부분은 “실패 이후의 복구 가능성”이었습니다.

- 주문 트랜잭션 내부에서 Outbox 기록
- 상태 관리: `INIT / SUCCESS / FAILED`
- FAILED 이벤트 재전송 스케줄링
- Consumer 멱등 처리

이를 통해 메시지 지연·실패 상황에서도 최종적으로 DB 재고가 맞춰지도록 구성했습니다.

---

# 아키텍처

- 멀티 모듈 구조
  - `api` : 요청 처리, Outbox 기록, Kafka Producer
  - `order` : 주문 / 재고 도메인
  - `item` : 상품 / 리뷰
  - `discount` : 할인 정책
  - `consumer` : Kafka Consumer
  - `pay` : 결제 연동
  - `common` : 공통 설정 및 Outbox 모델
  - `message` : 이벤트 계약

- Redis 기반 재고 감소 제어
- Outbox 기반 이벤트 발행
- Kafka Consumer를 통한 DB 반영

![SaleDay Server Architecture](https://github.com/user-attachments/assets/9ab94096-de35-4509-b29a-c060b7f20f70)

---

# 기술 스택

| 영역 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| DB/ORM | MySQL, Spring Data JPA, QueryDSL |
| Messaging | Kafka |
| Cache/Lock | Redis, Redisson |
| Build | Gradle Multi-module |
| Test | JUnit5, Mockito, Spring Test |
| Docs | Springdoc OpenAPI |

---

# 테스트 전략

## 단위 테스트 중심

- 주문 / 재고 / 할인 핵심 분기(성공·실패·보상) 검증
- Kafka, Redis, 외부 API는 mock 처리
- 빠른 피드백 루프 유지

## 통합 테스트 최소화

- 실제 장애 가능성이 높은 경계 구간만 선택 검증
  - Kafka 메시징
  - Redis 동시성
  - DB 반영

## 비동기 테스트 단순화

- 테스트 환경에서는 executor를 동기 방식으로 주입
- `CompletableFuture` 분기 흐름 예측 가능하게 구성

---

# 성능 테스트

- 1,000 TPS
- 100,000 요청
- 초기 재고 1,000,000
- 요청당 1 감소
- 싱글 인스턴스 기준

결과:

- 평균 응답 시간 약 170~190ms
- P99 약 900~1200ms
- 실패율 0%

정합성 검증 기준:

```
초기재고 - 주문수량합 = DB재고 = Redis재고
```

- Redis 재고 확인
- DB 재고 확인
- Outbox 처리 건수 확인
- Consumer 반영 건수 확인

성능 수치뿐 아니라 데이터 일치까지 검증했습니다.

자세한 내용:

- 부하 테스트 시나리오: [`./load-test/load-test.md`](./load-test/load-test.md)
- 실행 결과 리포트: [`./load-test/test.md`](./load-test/test.md)

---

# AI Agent 활용

AI는 설계를 대신하는 역할이 아니라, 다음 영역에서 활용했습니다.

- 테스트 코드 작성 가속
- 부하 테스트 반복 실행 자동화
- 설계 대안 비교 시 사고 확장
- 정합성 검증 리포트 정리

설계 기준 정의, 동기/비동기 경계 설정, 재처리 전략 수립, 최종 구조 결정은 직접 수행했습니다.

AI는 생산성과 검증 속도를 높이는 보조 도구로 활용했습니다.

---

# 프로젝트를 통해 얻은 것

- 정합성과 성능은 선택이 아니라 경계 설계의 문제라는 점
- 고트래픽 환경에서는 동기/비동기 분리가 핵심이라는 점
- 재처리를 포함한 설계가 운영 안정성에 중요하다는 점
- 성능 수치와 데이터 일치 검증을 함께 수행해야 한다는 점
- AI를 활용하되 설계 책임은 개발자가 가져가야 한다는 점