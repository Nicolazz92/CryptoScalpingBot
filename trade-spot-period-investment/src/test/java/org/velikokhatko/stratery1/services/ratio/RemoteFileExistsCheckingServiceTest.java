package org.velikokhatko.stratery1.services.ratio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.BaseStrategy1Test;

class RemoteFileExistsCheckingServiceTest extends BaseStrategy1Test {

    @Autowired
    private RemoteFileExistsCheckingService service;

    @Test
    void isURLReachable() {
        final String correctURL = "https://data.binance.vision/data/spot/monthly/klines/ADABUSD/1m/ADABUSD-1m-2021-09.zip";
        Assert.isTrue(service.isFileExists(correctURL), "Доступный заведомо ресурс не доступен");
        Assert.isTrue(!service.isFileExists(correctURL + "999"), "Недоступный заведомо ресурс доступен");
    }
}