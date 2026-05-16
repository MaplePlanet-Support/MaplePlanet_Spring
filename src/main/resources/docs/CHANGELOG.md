# CHANGELOG

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
