package org.velikokhatko.stratery1.constants;

import java.util.Arrays;

public enum UsdStablecoins {
    USDT, BUSD;

    public static boolean contains(String s) {
        return Arrays.stream(values()).map(UsdStablecoins::name).anyMatch(s::equals);
    }
}
