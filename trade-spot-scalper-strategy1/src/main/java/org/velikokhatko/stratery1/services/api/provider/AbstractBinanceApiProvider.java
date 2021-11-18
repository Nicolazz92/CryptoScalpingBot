package org.velikokhatko.stratery1.services.api.provider;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.market.TickerPrice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.velikokhatko.stratery1.constants.UsdStablecoins;
import org.velikokhatko.stratery1.exceptions.TraderBotException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.velikokhatko.stratery1.utils.Utils.assetBalanceListToString;

@Slf4j
public abstract class AbstractBinanceApiProvider {

    protected BinanceApiRestClient client;
    private UsdStablecoins bridgeCoin;

    public String getBalance() {
        Account account = client.getAccount();
        final List<AssetBalance> balances = account.getBalances().stream()
                .filter(balance -> Double.parseDouble(balance.getFree()) != 0 || Double.parseDouble(balance.getLocked()) != 0)
                .collect(Collectors.toList());

        try {
            List<AssetBalance> resultAsUSD = new ArrayList<>();
            double fullAmountUSD = 0;

            final List<TickerPrice> allPrices = client.getAllPrices();
            for (AssetBalance assetBalance : balances) {
                final String asset = assetBalance.getAsset();
                final double usdPrice;
                if (UsdStablecoins.contains(asset)) {
                    usdPrice = 1d;
                } else {
                    String usdPriceString = allPrices.stream()
                            .filter(tickerPrice -> tickerPrice.getSymbol().equals(asset + bridgeCoin.name()))
                            .findAny().orElseThrow(() -> new TraderBotException("Не получилось узнать $ курс валюты " + asset))
                            .getPrice();
                    usdPrice = Double.parseDouble(usdPriceString);
                }

                AssetBalance balanceUSD = new AssetBalance();
                balanceUSD.setAsset(assetBalance.getAsset());

                final double freeUSD = Double.parseDouble(assetBalance.getFree()) * usdPrice;
                fullAmountUSD += freeUSD;
                balanceUSD.setFree(String.valueOf(freeUSD));
                final double lockedUSD = Double.parseDouble(assetBalance.getLocked()) * usdPrice;
                fullAmountUSD += lockedUSD;
                balanceUSD.setLocked(String.valueOf(lockedUSD));

                resultAsUSD.add(balanceUSD);
            }
            return StringUtils.join("\n",
                    "USD balance:",
                    assetBalanceListToString(resultAsUSD),
                    "\nFull USD amount: " + new DecimalFormat("#.0#").format(fullAmountUSD));
        } catch (TraderBotException e) {
            log.error(e.getMessage());
            return StringUtils.join("\n", "Coin balance:", assetBalanceListToString(balances));
        }
    }

    public ExchangeInfo getExchangeInfo() {
        return client.getExchangeInfo();
    }

    @Value("${bridgeCoin}")
    public void setBridgeCoin(String bridgeCoin) {
        this.bridgeCoin = UsdStablecoins.valueOf(bridgeCoin);
    }
}
