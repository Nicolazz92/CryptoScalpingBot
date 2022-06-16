package org.velikokhatko.stratery1.services.api.custom;

import org.velikokhatko.stratery1.services.api.custom.domain.CoinInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractBinanceCustomApiServiceImpl implements BinanceCustomApi {
    protected BinanceCustomApiService binanceCustomApiService;

    @Override
    public List<CoinInfo> getAllCoinsInfo() {
        try {
            return binanceCustomApiService.getAllCoinsInfo(new Date().getTime()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO logger
        }
        return new ArrayList<>();
    }
}
