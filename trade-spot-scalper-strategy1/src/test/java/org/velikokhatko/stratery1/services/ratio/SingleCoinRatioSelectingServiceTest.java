package org.velikokhatko.stratery1.services.ratio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.BaseStrategy1Test;
import velikokhatko.dto.RatioParamsDTO;

import java.util.Optional;

class SingleCoinRatioSelectingServiceTest extends BaseStrategy1Test {

    @Autowired
    private SingleCoinRatioSelectingService service;

    @Test
    void selectRatio() throws InterruptedException {
        Optional<RatioParamsDTO> ratioParams = service.selectRatio("BONDBNB");
        Assert.isTrue(ratioParams.isEmpty(), "Откуда-то появились нескачанные данные");
        Thread.sleep(60000);
        ratioParams = service.selectRatio("BONDBNB");
        Assert.isTrue(ratioParams.isPresent(), "Данные не скачались");
        System.out.println(ratioParams.get());
    }
}