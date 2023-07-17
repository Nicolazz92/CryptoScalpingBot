package org.velikokhatko.stratery1.utils;

import org.junit.jupiter.api.Test;
import org.velikokhatko.stratery1.BaseStrategy1Test;

class UtilsTest extends BaseStrategy1Test {

    @Test
    void assetBalanceListToString() {
    }

    @Test
    void getKlinesZipURLBySymbol() {
        final String adabusd = Utils.getKlinesZipURLBySymbol("ADABUSD", 2);
        System.out.println(adabusd);
    }
}