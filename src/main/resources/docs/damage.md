# 메이플플래닛 무기 비교 및 데미지 계산 공식 정리

## 1. 물리 직업 (전사, 궁수, 도적, 해적)

* **최대 스탯 공격력(스공) 산출 공식**
    $$Max = (\text{주스탯} \times \text{무기상수} + \text{부스탯}) \times \frac{\text{총공격력}}{100}$$

---

## 2. 마법사 직업 (아크메이지, 비숍)

* **마법사 최종 데미지 공식 (기본형)**
    $$Damage = \left( \frac{\frac{\text{총마력}^2}{1000} + \text{총마력}}{30} + \frac{\text{총INT}}{200} \right) \times \text{스킬 공격력}$$

* [cite_start]**마법사 데미지 공식 (상수 단순화 버전)** [cite: 56, 670]
    $$\text{데미지} = (0.0033665 \times \text{합마}^2 + 3.3 \times \text{합마} + 0.5 \times \text{INT}) \times (1 + \text{총뎀}\%)$$

* [cite_start]**총 INT 산출 공식** [cite: 39, 56, 88]
    $$\text{총INT} = \text{순수INT} + \left( \text{순수INT} \times \frac{\text{INT}\%}{100} \right)$$

* [cite_start]**총 마력 산출 공식** [cite: 39, 57, 88]
    $$\text{총마력} = (\text{장비마력} + \text{총INT}) \times \left(1 + \frac{\text{마력}\%}{100}\right) + 20$$

* [cite_start]**스킬 데미지 산출 공식 (총뎀 합산 적용 시)** [cite: 39, 57, 88]
    $$\text{스킬데미지} = \frac{\text{스킬계수} + \text{총데미지}\%}{\text{스킬계수}}$$

* [cite_start]**마력% 잠재능력 데미지 상승률 상한** ($p$ = 증가하는 마력%) [cite: 57, 677]
    $$\text{데미지 상승률 상한} = (1+p)^2 - 1$$

---

## 3. 방어구 스탯 효율 계산 (자투 vs 직업모자)

* [cite_start]**잠재 3%의 깡스탯 환산 가치 공식** [cite: 58, 393]
    $$\text{잠재 3\%의 깡스텟 가치} = \frac{\text{기준 주스텟합} \times 0.03}{\text{현재 총배율}}$$

* [cite_start]**현재 총배율 산출 공식** [cite: 58, 393]
    $$\text{현재 총배율} = 1 + \text{기존 주스텟}\% + \text{현재 비교템의 주스텟}\%$$

---

## 4. 크리티컬 확률 및 데미지 기대값 (표도/허밋 기준)

* [cite_start]**스킬 데미지 1타당 기대값 산출 구조** [cite: 816]
    $$\text{스킬데미지 기대값} = (\text{노크리 확률} \times \text{기본 퍼뎀}) + (\text{크리티컬 확률} \times \text{크리티컬 시 퍼뎀})$$
    *(적용 예시: 노샾, 잠재 크확 8% 추가 시 럭키세븐 1타 기대값 = $0.42 \times 150 + 0.58 \times 250 = 208\%p$)* [cite: 816]