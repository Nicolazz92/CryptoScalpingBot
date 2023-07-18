package org.velikokhatko.period_invesment.services.api.provider;

import com.binance.api.client.BinanceApiClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.velikokhatko.period_invesment.services.api.custom.BinanceCustomApiService;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.createService;

@Service
@Profile("testnet")
public class TestnetBinanceApiProvider extends AbstractBinanceApiProvider {

    public TestnetBinanceApiProvider(@Value("${apikey}") String apiKey,
                                     @Value("${secretkey}") String secret) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret, true, false);
        binanceApiRestClient = factory.newRestClient();
        binanceCustomApiService = createService(BinanceCustomApiService.class, apiKey, secret);
    }
}
