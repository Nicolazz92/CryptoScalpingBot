package org.velikokhatko.stratery1.services.api.provider;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.constants.UsdStablecoins;
import org.velikokhatko.stratery1.exceptions.TraderBotException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.velikokhatko.stratery1.constants.Constants.DOUBLE_FORMAT;
import static org.velikokhatko.stratery1.constants.Constants.DOUBLE_VERBOSE_FORMAT;
import static org.velikokhatko.stratery1.utils.Utils.assetBalanceListToString;

@Slf4j
public abstract class AbstractBinanceApiProvider {

    protected BinanceApiRestClient client;
    private String bridgeCoin;

    public double getFreeBridgeCoinUSDBalance() {
        String bridgeSymbol = bridgeCoin + UsdStablecoins.BUSD.name();

        try {
            Double bridgeCoinUSDPrice = Double.valueOf(client.getPrice(bridgeSymbol).getPrice());

            Double bridgeCoinFreeAmount = getBalances().stream()
                    .filter(ab -> bridgeCoin.equals(ab.getAsset()))
                    .map(AssetBalance::getFree)
                    .map(Double::valueOf)
                    .findFirst().orElseThrow();

            return bridgeCoinFreeAmount * bridgeCoinUSDPrice;
        } catch (Exception e) {
            log.error("Не получилось узнать свободный $ баланс bridgeCoin: {}", bridgeSymbol, e);
            return Double.MIN_VALUE;
        }
    }

    public String getBalance() {
        final List<AssetBalance> balances = getBalances().stream()
                .filter(balance -> Double.parseDouble(balance.getFree()) != 0 || Double.parseDouble(balance.getLocked()) != 0)
                .collect(Collectors.toList());
        final String currentBridgeCoinBalance = balances.stream()
                .filter(ab -> bridgeCoin.equals(ab.getAsset()))
                .map(AssetBalance::getFree)
                .map(Double::valueOf)
                .map(DOUBLE_VERBOSE_FORMAT::format)
                .findFirst().orElse("undefined");

        try {
            List<AssetBalance> resultAsUSD = new ArrayList<>();
            double fullAmountUSD = 0;

            final List<TickerPrice> allPrices = getAllPrices();
            for (AssetBalance assetBalance : balances) {
                final String asset = assetBalance.getAsset();
                final double usdPrice;
                if (UsdStablecoins.contains(asset)) {
                    usdPrice = 1d;
                } else {
                    String usdPriceString = allPrices.stream()
                            .filter(tickerPrice -> tickerPrice.getSymbol().equals(asset + UsdStablecoins.BUSD.name()))
                            .findAny().orElseThrow(() -> new TraderBotException("Не получилось узнать $ курс валюты " + asset))
                            .getPrice();
                    usdPrice = Double.parseDouble(usdPriceString);
                }

                AssetBalance balanceUSD = new AssetBalance();
                balanceUSD.setAsset(assetBalance.getAsset());

                final double freeUSD = Double.parseDouble(assetBalance.getFree()) * usdPrice;
                fullAmountUSD += freeUSD;
                balanceUSD.setFree(DOUBLE_FORMAT.format(freeUSD));
                final double lockedUSD = Double.parseDouble(assetBalance.getLocked()) * usdPrice;
                fullAmountUSD += lockedUSD;
                balanceUSD.setLocked(DOUBLE_FORMAT.format(lockedUSD));

                resultAsUSD.add(balanceUSD);
            }
            return StringUtils.joinWith("\n",
                    "USD balance: ",
                    assetBalanceListToString(resultAsUSD),
                    "Full USD amount: " + DOUBLE_FORMAT.format(fullAmountUSD),
                    "Bridge coin current amount: " + currentBridgeCoinBalance + bridgeCoin);
        } catch (TraderBotException e) {
            log.error(e.getMessage());
            return StringUtils.joinWith("\n",
                    "Coin balance: ", assetBalanceListToString(balances),
                    "Bridge coin current amount: " + currentBridgeCoinBalance + bridgeCoin);
        }
    }

    public List<TickerPrice> getAllPrices() {
        try {
            return client.getAllPrices();
        } catch (BinanceApiException e) {
            log.error("Ошибка api: ", e);
        }
        return new ArrayList<>();
    }

    public Optional<TickerPrice> getPrice(String symbol) {
        try {
            return Optional.ofNullable(client.getPrice(symbol));
        } catch (BinanceApiException e) {
            log.error("Ошибка api: ", e);
        }
        return Optional.empty();
    }

    private List<AssetBalance> getBalances() {
        return client.getAccount().getBalances();
    }

    public ExchangeInfo getExchangeInfo() {
        return client.getExchangeInfo();
    }

    @Value("${bridgeCoin}")
    public void setBridgeCoin(String bridgeCoin) {
        Assert.hasText(bridgeCoin, "Не задана bridgeCoin");
        this.bridgeCoin = bridgeCoin;
    }
}
