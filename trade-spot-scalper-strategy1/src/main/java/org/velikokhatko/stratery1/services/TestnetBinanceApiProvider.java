package org.velikokhatko.stratery1.services;

import com.binance.api.client.BinanceApiClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("testnet")
public class TestnetBinanceApiProvider extends AbstractBinanceApiProvider {

    public TestnetBinanceApiProvider(@Value("${apikey}") String apiKey,
                                     @Value("${secretkey}") String secret) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret, true, false);
        client = factory.newRestClient();
    }
}
