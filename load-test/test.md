# SaleDay Single Instance 부하 테스트 결과

## 한눈에 요약
- 테스트 대상: `POST /order/stock/limit`
- 조건: `1000 users`, `spawn rate 1000/s`, `20s`, `host=http://localhost:9999`
- 최종 요청 수(Locust 콘솔): **19,382**
- 실패 수: **1 (0.01%)**
- 최종 처리량: **974.22 req/s**
- 최종 응답시간: **avg 239ms / median 19ms / max 3847ms**

## 실행 명령
```bash
../load-test/.venv/bin/locust -f ../load-test/SingleInstanceLoadTest.py \
  --headless -u 1000 -r 1000 --run-time 20s --host=http://localhost:9999 \
  --csv=/Users/chris/projects/load-test/single_instance_load --csv-full-history
```

## 응답시간 분포 (콘솔 최종)
- p50: **19ms**
- p66: **39ms**
- p75: **120ms**
- p80: **380ms**
- p90: **990ms**
- p95: **1400ms**
- p99: **2100ms**
- max: **3800ms**

## 실패 상세
- `LocustBadStatusCode(code=500)` 1건
- 참고: Locust는 실패가 1건이라도 있으면 종료 코드 `1` 반환

## 재고 정합성 검증 (DB + Redis)
아래는 테스트 후 실제 조회값입니다.

### DB 조회 결과
- `item_stock` (`code=1234`) 재고: **980,589**
- `order_item` 수량 합 (`code=1234`): **19,411**

### Redis 조회 결과
- `GET 1234`: **980,589**

### 정합성 계산
- 초기 재고: `1,000,000`
- 주문 수량 합: `19,411`
- 기대 재고: `1,000,000 - 19,411 = 980,589`
- 실제 DB 재고: `980,589`
- 실제 Redis 재고: `980,589`

## 정합성 결론
**DB 재고 = Redis 재고 = (초기재고 - 주문수량합)** 으로 일치합니다.
즉, 이번 실행 기준 재고 정합성은 정상입니다.

## 참고 (Outbox 상태)
- `SUCCESS: 13996`
- `INIT: 5415`
- `FAILED: 0`