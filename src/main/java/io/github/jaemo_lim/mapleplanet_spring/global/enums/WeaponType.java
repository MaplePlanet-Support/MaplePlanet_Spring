package io.github.jaemo_lim.mapleplanet_spring.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeaponType {

    ONE_HAND_SWORD("한손검", 1.20),
    ONE_HAND_AXE("한손도끼", 1.20),
    ONE_HAND_BLUNT("한손둔기", 1.20),
    TWO_HAND_SWORD("두손검", 1.34),
    TWO_HAND_AXE("두손도끼", 1.34),
    TWO_HAND_BLUNT("두손둔기", 1.34),
    SPEAR("창", 5.0),
    POLEARM("폴암", 5.0),
    BOW("활", 3.4),
    CROSSBOW("석궁", 3.6),
    DAGGER("단검", 3.6),
    CLAW("아대", 3.8),
    KNUCKLE("너클", 4.8),
    GUN("건", 3.6);

    private final String displayName;
    private final double constant;
}
