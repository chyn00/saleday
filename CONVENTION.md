# Schema 생성 규칙

## 1. Application Level 설정은 validate

- `ddl-auto: validate`  # 스키마 검증만 수행
- `develop`, `main` 브랜치 모두 스키마만 확인

**이유**: 배포단계에서 생성안된 테이블을 잡아내기 위함.  
(추후에 테스트 코드로 커버)

---

## 2. Entity 설계는 스크립트로 한다.

**이유**: DBA와의 협업이 필요하기 때문에, Script로 스키마 설계