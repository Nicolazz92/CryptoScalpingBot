package org.velikokhatko.period_invesment.constants;

import java.util.Arrays;

public enum BridgeCoins {
    BNB;

    public static boolean contains(String s) {
        return Arrays.stream(values()).map(BridgeCoins::name).anyMatch(s::equals);
    }
}
