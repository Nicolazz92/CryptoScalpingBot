package org.velikokhatko.stratery1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.velikokhatko.stratery1.services.AbstractBinanceApiProvider;

@SpringBootTest
@ActiveProfiles("testnet")
class TradeSpotScalperStrategy1ApplicationTests {

    @Autowired
    private AbstractBinanceApiProvider apiProvider;

    @Test
    void contextLoads() {
        System.out.println(apiProvider.getBalance());
    }

}
