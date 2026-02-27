# AI Agent 재사용 프롬프트 (Single Instance Load Test + 정합성 검증)

아래 프롬프트를 다음 세션에서 그대로 붙여 넣으면, 동일한 방식으로 테스트를 자동화할 수 있습니다.

---

## Prompt Template

```text
너는 지금 /Users/chris/projects/saleday 에서 작업해.

목표:
1) SingleInstanceLoadTest를 실행하고
2) DB/Redis 정합성을 검증하고
3) 결과를 load-test 폴더에 문서화해.

실행 조건:
- Leak 테스트는 하지 마.
- load-test 스크립트는 ../load-test/SingleInstanceLoadTest.py 를 사용.
- Locust 파라미터는 스크립트 주석 기준으로 실행:
  - --headless -u 1000 -r 1000 --run-time 20s --host=http://localhost:9999

필수 작업 순서:
1. 현재 docker 컨테이너 상태 확인(docker ps)
2. API/Consumer가 죽어 있으면 docker compose up -d --build로 기동
3. locust 실행 후 결과 파일 생성
4. MySQL/Redis select 쿼리로 재고 정합성 확인
5. 결과를 /Users/chris/projects/saleday/load-test/test.md 에 업데이트

결과 파일 경로:
- /Users/chris/projects/saleday/load-test/single_instance_load.log
- /Users/chris/projects/saleday/load-test/single_instance_load_stats.csv
- /Users/chris/projects/saleday/load-test/single_instance_load_stats_history.csv
- /Users/chris/projects/saleday/load-test/single_instance_load_failures.csv
- /Users/chris/projects/saleday/load-test/single_instance_load_exceptions.csv
- /Users/chris/projects/saleday/load-test/test.md

Locust 실행 명령:
../load-test/.venv/bin/locust -f ../load-test/SingleInstanceLoadTest.py \
  --headless -u 1000 -r 1000 --run-time 20s --host=http://localhost:9999 \
  --csv=/Users/chris/projects/saleday/load-test/single_instance_load --csv-full-history \
  > /Users/chris/projects/saleday/load-test/single_instance_load.log 2>&1

정합성 검증 SQL(예: itemCode=1234):
- item_stock 조회
- order_item quantity 합 조회
- outbox 상태 집계 조회

예시 쿼리:
SELECT i.id, i.code, s.quantity AS db_stock
FROM item i JOIN item_stock s ON s.item_id=i.id
WHERE i.code='1234';

SELECT COALESCE(SUM(oi.quantity),0) AS ordered_qty_1234
FROM order_item oi JOIN item i ON oi.item_id=i.id
WHERE i.code='1234';

SELECT status, COUNT(*) AS cnt
FROM outbox_message
WHERE type='stock'
GROUP BY status
ORDER BY status;

Redis 조회:
docker exec myredis redis-cli GET 1234

리포트 필수 포함 항목:
- 테스트 조건(u/r/run-time)
- 총 요청 수, 실패 수, req/s, avg/median/max
- percentile(p50/p90/p95/p99)
- 실패 타입 요약
- 초기재고 - 주문수량합 = DB재고 = Redis재고 계산식
- 정합성 결론(일치/불일치)

주의:
- 실패가 1건 이상이면 locust exit code가 1일 수 있음. 이 경우 로그/CSV를 읽고 결과를 해석해서 문서화해.
- 결과는 반드시 숫자로 근거를 남겨.
```

---

## 빠른 실행 체크리스트

- [ ] `docker ps`에서 `saleday-api`, `saleday-consumer`, `kafka`, `myredis`, `mysql-container` 확인
- [ ] Locust 실행 완료
- [ ] CSV/LOG 생성 확인
- [ ] DB/Redis 정합성 조회 완료
- [ ] `load-test/test.md` 업데이트 완료
