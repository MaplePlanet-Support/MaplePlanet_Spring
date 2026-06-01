package io.github.jaemo_lim.mapleplanet_spring.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeaponType {

    ONE_HAND_SWORD("한손검", 4.0),
    ONE_HAND_AXE("한손도끼", 4.4),
    ONE_HAND_BLUNT("한손둔기", 4.4),
    TWO_HAND_SWORD("두손검", 4.6),
    TWO_HAND_AXE("두손도끼", 4.8),
    TWO_HAND_BLUNT("두손둔기", 4.8),
    SPEAR("창", 5.0),
    POLEARM("폴암", 5.0),
    BOW("활", 3.8),
    CROSSBOW("석궁", 3.6),
    DAGGER("단검", 3.6),
    CLAW("아대", 3.6),
    KNUCKLE("너클", 4.8),
    GUN("건", 3.6),
    WAND("완드", 0.0),
    STAFF("스태프", 0.0);

    private final String displayName;
    private final double constant;

    public boolean isMagicWeapon() {
        return this == WAND || this == STAFF;
    }
}
