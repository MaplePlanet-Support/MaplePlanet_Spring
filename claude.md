# CLAUDE.md

Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

---

**These guidelines are working if:** fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, and clarifying questions come before implementation rather than after mistakes.

---

## Project: 메이플 플래닛 유틸리티 플랫폼

### 개요
메이플 플래닛(사설 서버) 유저를 위한 스펙업 유틸리티 사이트.
비로그인 기반. 주요 기능: 아이템 비교, 스공/데미지 비교, 프리셋 저장.

### 기술 스택
- **언어/프레임워크:** Java 21, Spring Boot 3.5.x
- **ORM:** Spring Data JPA (Hibernate)
- **DB:** MySQL (운영) / H2 (로컬 개발)
- **캐시/제한:** Caffeine 기반 API rate limit
- **빌드:** Gradle (Groovy DSL)
- **기본 패키지:** `io.github.jaemo_lim.mapleplanet_spring`

### 패키지 구조
```
src/main/java/io/github/jaemo_lim/mapleplanet_spring/
├── domain/
│   ├── calculator/  # 스탯/스공/데미지 계산 API
│   └── preset/      # 프리셋 저장/불러오기 API
├── global/          # 공통 (exception, response, config)
└── MaplePlanetSpringApplication.java
```

### 핵심 계산 공식
```
# 최종 주스탯
최종 주스탯 = (직접 찍은 주스탯 + Σ깡주스탯) × (1 + Σ주스탯% / 100)

# 스공
스공 = (주스탯 × 무기상수 + 부스탯) × 총공격력 / 100

# 아이템 교체 효율%
효율% = (착용후 최종 주스탯 - 미착용 최종 주스탯) / 직접 찍은 주스탯 × 100

# 부스탯 → 주스탯 환산
부스탯 1 = 1 / 무기상수 주스탯

# 공격력 → 주스탯 환산
공격력 1 = (주스탯 × 무기상수 + 부스탯) / (무기상수 × 총공격력) 주스탯
```

### 직업별 스탯
| 직업 | 주스탯 | 부스탯 | 공격 |
|------|--------|--------|------|
| 전사 | STR | DEX | ATK |
| 도적 | LUK | STR | ATK |
| 궁수 | DEX | STR | ATK |
| 해적 | STR | DEX | ATK |
| 마법사 | INT | LUK | MATK |

### 비로그인 사용자 처리
- 닉네임과 선택 비밀번호 기반 프리셋 저장/불러오기

### 빌드 / 실행
```bash
./gradlew build          # 빌드
./gradlew bootRun        # 로컬 실행 (H2)
./gradlew test           # 테스트
```
