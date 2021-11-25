package org.velikokhatko.stratery1.services.ratio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.velikokhatko.stratery1.BaseStrategy1Test;
import org.velikokhatko.stratery1.services.api.exchange.SymbolInfoShort;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;

class SingleCoinRatioSelectingServiceTest extends BaseStrategy1Test {

    @Autowired
    private SingleCoinRatioSelectingService service;

    @Test
    void selectRatio() {
        final SymbolInfoShort symbolInfo = new SymbolInfoShort();
        symbolInfo.setSymbol("BONDBNB");
        final RatioParams ratioParams = service.selectRatio(symbolInfo);
        System.out.println(ratioParams);
    }
}