package org.velikokhatko.stratery1.services.api.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.createService;

@Service
@Profile("testnet")
public class TestnetBinanceCustomApiServiceImpl extends AbstractBinanceCustomApiServiceImpl {

    public TestnetBinanceCustomApiServiceImpl(@Value("${apikey}") String apiKey,
                                              @Value("${secretkey}") String secret) {
        binanceCustomApiService = createService(BinanceCustomApiService.class, apiKey, secret);
    }
}
