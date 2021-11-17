package org.velikokhatko.stratery1.constants;

import java.util.Arrays;

public enum Stablecoins {
    USDT, BUSD;

    public static boolean contains(String s) {
        return Arrays.stream(values()).map(Stablecoins::name).anyMatch(s::equals);
    }
}
