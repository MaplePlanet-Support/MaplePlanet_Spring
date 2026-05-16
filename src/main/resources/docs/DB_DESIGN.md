# 메이플 플래닛 DB 설계 문서

## 개요

비로그인 기반 유틸리티 플랫폼. 닉네임/PIN으로 캐릭터 프로필을 저장하며, 향후 정식 로그인 도입 예정.

---

## 테이블 목록

| 테이블 | 용도 | 버전 |
|--------|------|------|
| `job` | 직업 참조 데이터 | V1.0 |
| `weapon_constant` | 무기 상수 참조 데이터 | V1.0 |
| `user_profile` | 유저 식별 (닉네임 + PIN) | V1.0 |
| `character` | 캐릭터 스탯/장비 프로필 | V1.0 |
| `equipped_item` | 캐릭터 장착 아이템 스탯 | V1.0 |
| `character_skill` | 캐릭터 스킬 레벨 | V1.0 |
| `calculation_result` | 계산 결과 저장/공유 | V1.0 |
| `skill` | 스킬 참조 데이터 | V1.0 stub → V2.0 확장 |
| `monster` | 몬스터 참조 데이터 | V2.0 |

---

## 테이블 상세

### `job` — 직업

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| name | VARCHAR(20) | NOT NULL, UNIQUE | 전사, 도적, 궁수, 해적, 마법사 |
| main_stat | VARCHAR(10) | NOT NULL | STR / LUK / DEX / STR / INT |
| sub_stat | VARCHAR(10) | NOT NULL | DEX / STR / STR / DEX / LUK |
| attack_stat | VARCHAR(10) | NOT NULL | ATK / ATK / ATK / ATK / MATK |

---

### `weapon_constant` — 무기 상수

스공 공식: `(주스탯 × constant + 부스탯) × 총공격력 / 100`

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| weapon_type | VARCHAR(30) | NOT NULL, UNIQUE | 한손검, 두손검, 활, 단궁, 완드, 스태프 등 |
| constant | DOUBLE | NOT NULL | 무기 상수 (ex: 1.2, 1.3) |

---

### `user_profile` — 유저 프로필

로그인 없이 닉네임 + 4자리 PIN으로 본인 인증. 향후 OAuth 로그인으로 전환 예정.

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| identifier | VARCHAR(30) | NOT NULL, UNIQUE | 유저가 설정한 닉네임 or ID |
| pin_hash | VARCHAR(60) | NOT NULL | BCrypt 해시된 PIN |
| created_at | DATETIME | NOT NULL | |

---

### `character` — 캐릭터

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| user_profile_id | BIGINT | FK(user_profile), NULL 허용 | 비로그인 시 null |
| job_id | BIGINT | FK(job), NOT NULL | |
| weapon_constant_id | BIGINT | FK(weapon_constant), NOT NULL | |
| name | VARCHAR(20) | NOT NULL | 캐릭터명 |
| level | INT | NOT NULL | |
| base_main_stat | INT | NOT NULL | 스탯 창에서 직접 찍은 주스탯 |
| base_sub_stat | INT | NOT NULL | 스탯 창에서 직접 찍은 부스탯 |
| created_at | DATETIME | NOT NULL | |
| updated_at | DATETIME | NOT NULL | |

---

### `equipped_item` — 장착 아이템

유저가 직접 스탯을 입력해 저장. 아이템 DB 카탈로그 없음.

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| character_id | BIGINT | FK(character), NOT NULL | |
| slot | VARCHAR(15) | NOT NULL | EquipSlot enum 값 |
| item_name | VARCHAR(60) | NOT NULL | |
| str_flat | INT | NOT NULL, DEFAULT 0 | 깡 STR |
| dex_flat | INT | NOT NULL, DEFAULT 0 | 깡 DEX |
| int_flat | INT | NOT NULL, DEFAULT 0 | 깡 INT |
| luk_flat | INT | NOT NULL, DEFAULT 0 | 깡 LUK |
| atk_flat | INT | NOT NULL, DEFAULT 0 | 깡 공격력 |
| matk_flat | INT | NOT NULL, DEFAULT 0 | 깡 마력 |
| str_percent | INT | NOT NULL, DEFAULT 0 | %STR |
| dex_percent | INT | NOT NULL, DEFAULT 0 | %DEX |
| int_percent | INT | NOT NULL, DEFAULT 0 | %INT |
| luk_percent | INT | NOT NULL, DEFAULT 0 | %LUK |
| atk_percent | INT | NOT NULL, DEFAULT 0 | %공격력 |
| matk_percent | INT | NOT NULL, DEFAULT 0 | %마력 |

**EquipSlot 값:** WEAPON, HAT, OVERALL, TOP, BOTTOM, GLOVE, SHOE, SHIELD, EARRING, CAPE, PENDANT_1, PENDANT_2, EYEWEAR, RING_1~4, BELT, MEDAL

---

### `skill` — 스킬 (V1.0 stub)

V1.0에서는 스킬레벨 저장용 참조 테이블로만 사용. V2.0에서 `damage_percent`, `max_hits` 컬럼 추가 예정.

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| job_id | BIGINT | FK(job), NOT NULL | |
| name | VARCHAR(50) | NOT NULL | |
| max_level | INT | NOT NULL | |

---

### `character_skill` — 캐릭터 스킬 레벨

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| character_id | BIGINT | FK(character), NOT NULL | |
| skill_id | BIGINT | FK(skill), NOT NULL | |
| skill_level | INT | NOT NULL | |

---

### `calculation_result` — 계산 결과

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| character_id | BIGINT | FK(character), NULL 허용 | 프로필 없이 계산만 할 경우 null |
| share_key | VARCHAR(36) | NOT NULL, UNIQUE | 공유 링크용 UUID |
| calculation_type | VARCHAR(20) | NOT NULL | STAT_COMPARISON, HUNT_CALCULATOR |
| input_snapshot | TEXT | NOT NULL | 당시 입력값 JSON |
| output_snapshot | TEXT | NOT NULL | 계산 결과 JSON |
| created_at | DATETIME | NOT NULL | |

---

### `monster` — 몬스터 (V2.0)

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | |
| name | VARCHAR(50) | NOT NULL | |
| level | INT | NOT NULL | |
| hp | BIGINT | NOT NULL | |
| defense_percent | INT | NOT NULL | 방어율 (0~100) |
| element | VARCHAR(20) | NULL 허용 | 무속성이면 null |
| map_name | VARCHAR(50) | NOT NULL | 사냥터명 |

---

## ERD (텍스트)

```
user_profile ──< character >── job
                    │               └── weapon_constant
                    ├──< equipped_item
                    └──< character_skill >── skill >── job

calculation_result >── character (nullable)
monster (독립 테이블, V2.0)
```

---

## 버전별 변경 계획

### V1.0 완료
- `job`, `weapon_constant`, `user_profile`, `character`, `equipped_item`, `character_skill`, `calculation_result`, `skill`(stub)

### V2.0 추가
- `monster` 테이블 데이터 투입
- `skill` 테이블에 `damage_percent`, `max_hits` 컬럼 추가
- n방컷 계산 로직 구현

### V3.0 백로그 (별도 설계 예정)
- 혼돈의 주문서 시뮬레이터
- 큐브 시뮬레이터
- 메이커 시뮬레이터
