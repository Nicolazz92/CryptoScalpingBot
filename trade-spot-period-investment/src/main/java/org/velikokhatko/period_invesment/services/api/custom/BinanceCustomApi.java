package org.velikokhatko.period_invesment.services.api.custom;

import org.velikokhatko.period_invesment.services.api.custom.domain.CoinInfo;

import java.util.List;

public interface BinanceCustomApi {

    /**
     * https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
     */
    List<CoinInfo> getAllCoinsInfo();
}
