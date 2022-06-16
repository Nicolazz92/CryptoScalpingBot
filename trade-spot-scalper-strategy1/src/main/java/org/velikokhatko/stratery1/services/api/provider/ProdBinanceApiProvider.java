package org.velikokhatko.stratery1.services.api.provider;

import com.binance.api.client.BinanceApiClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.api.custom.BinanceCustomApi;
import org.velikokhatko.stratery1.services.api.custom.BinanceCustomApiService;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.createService;

@Service
@Profile("production")
public class ProdBinanceApiProvider extends AbstractBinanceApiProvider {

    public ProdBinanceApiProvider(@Value("${apikey}") String apiKey,
                                  @Value("${secretkey}") String secret) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret);
        binanceApiRestClient = factory.newRestClient();
        binanceCustomApiService = createService(BinanceCustomApiService.class, apiKey, secret);
    }
}
