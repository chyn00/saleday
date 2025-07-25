e-commerce backend project

이 프로젝트는 바로 실행이 어려울 수 있습니다.(AWS IAM과 같은 일부 설정이 local yaml에 의해 가려져 있습니다.)

# SaleDay - 고성능 커머스 백엔드 시스템

**SaleDay**는 대규모 트래픽 환경에서도 안정적으로 동작하는 커머스 백엔드 시스템을 목표로 설계된 개인 프로젝트입니다.  
실무에서 마주한 문제의식을 바탕으로, 객체지향 원칙, 대용량 트래픽 대응, 분산 환경에서의 트랜잭션 처리 등 **현실적인 기술 스택과 구조**를 기반으로 구현했습니다.

---

## 프로젝트 개요

- **개발 기간**: 2025.03.24 ~ 진행중
- **목표**:
    - 실무 기반의 트래픽 대응 구조 설계 및 구현
    - 다양한 할인 정책을 유연하게 처리하는 OOP 설계
    - Kafka 기반 비동기 메시징, Redis 기반 동시성 제어 적용
    - 메세지 큐 처리, 계층 분리 등 구조적 설계 원칙을 고려한 아키텍처 구성

---

## 기술 스택

| 분야 | 기술                                           |
|------|----------------------------------------------|
| Language | Java 17                                      |
| Framework | Spring Boot 3.x |
| ORM & DB | JPA (Hibernate), QueryDSL, MySQL             |
| Concurrency & Locking | Redis (`INCR`, `DECR` 등 원자 연산 기반 동시성 제어)     |
| Messaging | Kafka (Outbox 기반 이벤트 발행 및 처리)                |
| Infra | Docker, Docker Compose                       |
| Build | Gradle (Multi-module)                        |
| Test | JUnit5 (실제 Bean 기반 통합 및 단위 테스트), Locust 부하테스트              |
| 문서화 | Swagger (Springdoc OpenAPI)                   |

---

## 아키텍처

- **멀티모듈 구조**
    - `:api` – 외부 요청 처리 및 Swagger 제공
    - `:domain` – 도메인 로직 및 서비스 계층
    - `:consumer` – Kafka 기반 비동기 처리 담당
- **계층 분리를 고려한 설계 구조**
- **Redis 기반 재고 감소 동시성 제어 (`INCR`, `DECR`)**
- **Outbox 패턴 기반 Kafka 메시지 전송으로 재고 처리 분리**

---

[SaleDay Server Architecture]<img width="1270" height="710" alt="image" src="https://github.com/user-attachments/assets/9ab94096-de35-4509-b29a-c060b7f20f70" />



## 주요 기능

### 주문 기능
- **단건 주문 처리 구조**: 선착순 이벤트 및 재고 한정 상황에 최적화
    - Kafka + Redis 기반 비동기 처리로 정합성과 동시성 모두 확보
- **다건 주문 처리 (장바구니)**: 일반 상품에 대해 `BulkRequest` 기반으로 트랜잭션 내 일괄 저장 처리
    - 메시징 불필요, DB 트랜잭션만으로 충분한 정합성 보장
- 할인 정책 적용 및 주문 금액 계산
- 트랜잭션 내 `Order -> OrderItem` 일괄 처리

### 할인 정책
- 전략 패턴 기반 할인 정책 선택기 구현
- 고정 할인, 비율 할인 등 정책 확장 가능

### 동시성 재고 처리
- 주문 요청 시 Redis에서 `DECR`를 활용해 병렬 재고 수량 제어
- 주문 커밋 후 Outbox 이벤트 발행 및 Kafka 메시지 전송
- Kafka Consumer에서 실제 재고 DB 반영 처리
- **정합성과 고속성**을 모두 확보한 재고 처리 구조 구현

### 비동기 메시징
- 주문 성공 후 Kafka를 통해 `stock.decreased` 이벤트 발행
- Consumer는 별도 모듈로 분리하여 비동기 처리

---

## 설계 포인트

- **단건 주문 기반 트래픽 설계**: 선착순 이벤트, 재고 한정 상황에서 고속 트래픽 처리를 위해 단건 주문 구조를 채택하고, Kafka + Redis로 비동기 처리
- **다건 주문과 단건 주문 분리 설계**: 일반 상품 주문은 `BulkRequest`와 트랜잭션으로 처리, 메시징 구조 없이 단순하게 유지
- **객체지향**: 할인 정책, 주문 로직을 전략 패턴과 도메인 중심 설계로 분리
- **트래픽 대응**: Redis 기반 원자 연산 + Kafka 이벤트 구조를 통해 TPS 처리 향상
- **정합성과 고속성의 균형**: 재고수량이 한정되어 있는 등의 동시성이 필요한 주문 요청 시 Redis로 병렬 제어 → 커밋 후 Outbox 이벤트 발행 및 Kafka Publish → Consumer에서 재고 DB 반영
- **신뢰성**: `@TransactionalEventListener` 및 Outbox Pattern을 활용하여 주문 커밋 이후에만 메시지 발행
- **확장성**: 할인 정책, 이벤트 핸들링, 메시지 채널 등을 쉽게 확장 가능하도록 설계
- **테스트**: 실제 Bean 기반의 테스트 환경 구성

## 성능 테스트 결과
자세한 성능 테스트 결과는 [load-test.md](./load-test.md)를 참고하세요.

## ✅ Kafka 병목 및 Outbox 안정성 개선 (2025.07)

Kafka 전송 시 간헐적 TimeoutException, 전송 실패가 발생해 Outbox 기반 아키텍처를 구조적으로 개선했습니다.

### 개선 내용(2025.07.26)

- **Outbox 메시지 처리 흐름 분리**
  - 트랜잭션 커밋 전(Before Commit): 모든 메시지를 `INIT` 상태로 기록
  - Kafka 전송 결과를 CompletableFuture 콜백에서 ACK를 받아서 JPA `@Transactional` 기반으로 `SUCCESS` / `FAIL` 상태 마킹
  - 콜백 안에서 DB 접근해도 문제 없도록 트랜잭션 경계 명확히 보장

- **Kafka 병목 해소**
  - `acks=all` → `acks=1` 변경: 복제 대기 제거로 전송 속도 향상
  - 기본 Sticky 파티셔너 → `RoundRobinPartitioner` 설정 변경: 메시지 분산으로 특정 파티션 몰림 방지
  - 전송 중단 없이 Timeout 문제 전면 해결

- **실패 메시지 처리 보완**
  - 주기적 Polling Scheduler를 통해 `FAIL` 상태 메시지 재전송 처리

### 개선 결과(2025.07.26)

- TPS 하락 없이 Kafka 안정성 확보
- 이벤트 전송 흐름과 DB 트랜잭션 경계 분리
- 운영 가시성과 장애 대응력 향상


## Swagger
http://localhost:9999/swagger-ui/index.html




