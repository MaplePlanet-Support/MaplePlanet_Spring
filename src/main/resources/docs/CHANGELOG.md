# CHANGELOG

## [v0.4] - 2026-05-16
### Added
- `WeaponType` enum — 전체 무기상수 테이블 (인게임 실측 검증 완료)
- `POST /api/v1/calculator/damage` — 스공 계산 API (weaponType으로 상수 자동 적용)
- `DamageCalcRequest/Response` DTO
- `DamageCalcService` — 정축/여축 레벨 → 공격력 반영, 스공 계산

---

## [v0.3] - 2026-05-16
### Added
- `POST /api/v1/calculator/stat-compare` — 아이템 주스탯 비교 API
- `StatCompareRequest`, `StatCompareResponse`, `ItemStatDto` DTO
- `StatCompareService` — `(baseStat + 깡스탯합) × (1 + 스탯%합 / 100)` 공식 적용

---

## [v0.2] - 2026-05-16
### Added
- `UserProfile` entity — 비로그인 익명 프로필 (identifier, pinHash)
- `Character` entity — 직업/무기/스탯/장비/스킬 연관
- `EquippedItem` entity — 슬롯별 flat/percent 스탯 분리
- `CharacterSkill` entity — 캐릭터-스킬 레벨 매핑
- `Skill` entity — 직업별 스킬 스텁 (V2.0)
- `Monster` entity — 몬스터 정보 스텁 (V2.0)
- `CalculationResult` entity — shareKey(UUID) 기반 계산 결과 저장
- `EquipSlot` enum — 장비 슬롯 열거형
- `CalculationType` enum — STAT_COMPARISON, HUNT_CALCULATOR

### Removed
- `Item`, `ItemBaseStat` entity
- `SimulationResult` entity
- `CubeGradeProbability`, `CubeOptionProbability` entity
- `MakerMaterial`, `MakerRecipe`, `MakerResultOption` entity
- `ScrollProbability` entity
- `ItemGrade`, `ItemType`, `OptionTier`, `SimulationType` enum

---

## [v0.1] - 2026-05-16
### Added
- 프로젝트 초기 세팅 (Spring Boot 3.5, JPA, H2, Caffeine)
- 기본 패키지 구조 생성
- `Job`, `WeaponConstant` entity
