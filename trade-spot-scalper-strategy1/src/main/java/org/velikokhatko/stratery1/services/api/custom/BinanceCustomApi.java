package org.velikokhatko.stratery1.services.api.custom;


import velikokhatko.dto.CoinInfoDTO;

import java.util.List;

public interface BinanceCustomApi {

    /**
     * https://binance-docs.github.io/apidocs/spot/en/#all-coins-39-information-user_data
     */
    List<CoinInfoDTO> getAllCoinsInfo();
}
