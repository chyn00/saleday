#Table 설계
유저 1억명 가정

#User 테이블 구조
+-------------------+---------------------+--------------------------------+----------------------------------------+
| 컬럼명(Column Name)| 데이터 타입(Data Type)| 제약 조건(Constraints)         | 설명(Description)                     |
+-------------------+---------------------+--------------------------------+----------------------------------------+
| userId            | INT                | PRIMARY KEY, AUTO_INCREMENT   | 각 사용자를 위한 고유 ID              |
| loginId           | VARCHAR(50)        | NOT NULL, UNIQUE              | 사용자의 고유 로그인 ID               |
| password          | VARCHAR(100)       | NOT NULL                      | 해시된 비밀번호                       |
| createdAt         | TIMESTAMP          | NOT NULL                      | 레코드 생성 시간                     |
| updatedAt         | TIMESTAMP          | NOT NULL                      | 마지막 수정 시간                     |
| registerId        | VARCHAR(50)        | NOT NULL                      | 레코드를 생성한 사용자의 ID           |
| modificationId    | VARCHAR(50)        | NOT NULL                      | 마지막으로 레코드를 수정한 사용자의 ID |
+-------------------+---------------------+--------------------------------+----------------------------------------+

CREATE TABLE user (
userId INT AUTO_INCREMENT PRIMARY KEY,        -- 사용자 ID (Primary Key, 자동 증가)
loginId VARCHAR(50) NOT NULL UNIQUE,         -- 로그인 ID (고유 값)
password VARCHAR(100) NOT NULL,              -- 비밀번호 (해시 값)
createdAt TIMESTAMP NOT NULL,                -- 등록 날짜 및 시간
updatedAt TIMESTAMP NOT NULL,                -- 수정 날짜 및 시간
registerId VARCHAR(50) NOT NULL,             -- 등록자 ID (로그인 ID와 동일한 정책)
modificationId VARCHAR(50) NOT NULL          -- 수정자 ID (로그인 ID와 동일한 정책)
);
