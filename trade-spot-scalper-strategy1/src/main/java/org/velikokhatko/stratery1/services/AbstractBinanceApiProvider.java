package org.velikokhatko.stratery1.services;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.market.TickerPrice;
import lombok.extern.slf4j.Slf4j;
import org.velikokhatko.stratery1.constants.Stablecoins;
import org.velikokhatko.stratery1.exceptions.TraderBotException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.velikokhatko.stratery1.utils.Utils.assetBalanceToString;

@Slf4j
public abstract class AbstractBinanceApiProvider {

    protected BinanceApiRestClient client;

    public String getBalance() {
        Account account = client.getAccount();
        final List<AssetBalance> balances = account.getBalances();

        try {
            List<AssetBalance> resultAsUSD = new ArrayList<>();
            double fullAmountUSD = 0;

            final List<TickerPrice> allPrices = client.getAllPrices();
            for (AssetBalance assetBalance : balances) {
                final String symbol = assetBalance.getAsset();
                final double usdPrice;
                if (Stablecoins.contains(symbol)) {
                    usdPrice = 1d;
                } else {
                    String usdPriceString = allPrices.stream().filter(tickerPrice -> tickerPrice.getSymbol().equals(symbol + "BUSD"))
                            .findAny().orElseThrow(() -> new TraderBotException("Не получилось узнать $ курс валюты " + symbol))
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
            return "USD balance: \n"
                    + assetBalanceToString(resultAsUSD)
                    + "\nFull USD amount: " + new DecimalFormat("#.0#").format(fullAmountUSD);
        } catch (TraderBotException e) {
            log.error(e.getMessage());
            return assetBalanceToString(balances);
        }

    }
}
