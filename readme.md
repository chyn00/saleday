# SaleDay

사용자가 몰릴 때도 주문과 재고가 틀어지지 않게 만드는 커머스 백엔드 프로젝트입니다.  
핵심 목표는 **데이터 정확성(주문/재고)** 과 **빠른 처리 속도**를 함께 잡는 것입니다.

## 시작 전 안내

- 일부 설정은 `local` 프로파일에 의존하므로 바로 실행이 어려울 수 있습니다.
- 인프라(Kafka/Redis/MySQL)가 준비되지 않으면 일부 기능은 동작하지 않습니다.

## 실행 방법 (Docker Compose)

프로젝트 루트에서 아래 명령으로 실행할 수 있습니다.

```bash
docker compose up -d --build
```

실행 후 확인:

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

## 강조 포인트

1. **재고가 적은 상품 주문 처리**
- Redis `DECR`로 먼저 재고를 줄여서 동시 요청 충돌을 줄임
- 주문이 DB에 정상 저장된 뒤에만 Kafka 이벤트 발행
- Consumer가 최종 재고를 DB에 반영

2. **안전한 비동기 처리**
- 주문 커밋 이후에만 이벤트 발행(`@TransactionalEventListener(AFTER_COMMIT)`)
- Outbox 상태(`INIT/SUCCESS/FAILED`)로 전송 결과 추적
- 실패한 메시지는 스케줄러로 재시도

3. **확장 가능한 도메인 설계**
- 할인 정책은 전략 패턴으로 분리
- 모듈 단위 책임 분리(`api`, `order`, `item`, `discount`, `consumer`, `pay`)

## 기술 스택

| 영역 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| DB/ORM | MySQL, Spring Data JPA, QueryDSL |
| Messaging | Kafka |
| Cache/Lock | Redis, Redisson |
| Build | Gradle Multi-module |
| Test | JUnit5, Mockito, Spring Test |
| Docs | Springdoc OpenAPI (Swagger) |

## 모듈 구조

- `api`: 외부 API, 오케스트레이션, Outbox/Kafka Producer
- `order`: 주문/재고 도메인 및 서비스
- `item`: 상품/리뷰 도메인 및 서비스
- `discount`: 할인 정책 계산
- `consumer`: Kafka Consumer, 재고 차감 반영
- `pay`: 결제 연동
- `common`: 공통 예외, JPA 설정, Outbox 모델
- `message`: pub/sub 메시지 계약

## 아키텍처 핵심 흐름

1. 주문 요청
2. Redis 재고 선차감(`DECR`)
3. 주문 트랜잭션 커밋
4. Outbox 저장(BEFORE_COMMIT) + 이벤트 발행(AFTER_COMMIT)
5. Kafka Consumer가 DB 재고 반영

[SaleDay Server Architecture]<img width="1270" height="710" alt="image" src="https://github.com/user-attachments/assets/9ab94096-de35-4509-b29a-c060b7f20f70" />

## 테스트 방식

테스트는 빠르게 돌리면서도, 중요한 로직을 놓치지 않게 구성했습니다.

### 1) 대부분은 단위 테스트로 검증

- 주문/재고/할인/리뷰의 핵심 분기(성공, 실패, 보상)를 단위 테스트로 확인
- Kafka, Redis, 락, 외부 API는 mock으로 대체해 테스트를 빠르고 안정적으로 실행
- 코드 수정 후 문제를 빨리 찾을 수 있도록 구성

### 2) 통합 테스트는 꼭 필요한 구간만

- 모든 기능을 통합 테스트로 만들지는 않음
- 실제 장애 가능성이 큰 경계만 선택해서 검증
- 예: Kafka 메시징, Redis/락 동시성, DB 반영

### 3) 비동기 코드는 테스트를 단순하게

비동기 코드는 타이밍 때문에 테스트가 불안정해지기 쉬워서, 테스트에서는 순서를 단순하게 고정했습니다.

- 단위 테스트에서는 executor를 동기 방식으로 넣어서 콜백을 바로 실행
- `CompletableFuture`의 성공/실패 분기를 예측 가능한 방식으로 검증
- 실제 스레드풀 동작은 통합/운영 환경에서 따로 확인

## 최근 개선 사항 (Kafka/Outbox)

- Outbox 상태 업데이트를 Kafka 전송 콜백에서 처리
- 실패 메시지 재전송 스케줄링 유지
- 전송 병목 완화를 위한 Kafka 설정 조정(`acks=1`)

## 성능 테스트

- 부하 테스트 시나리오: [`./load-test/load-test.md`](./load-test/load-test.md)
- 단일 인스턴스 실행 결과 리포트: [`./load-test/test.md`](./load-test/test.md)
