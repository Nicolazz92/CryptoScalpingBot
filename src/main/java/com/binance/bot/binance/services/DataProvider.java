package com.binance.bot.binance.services;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.OrderBook;
import com.binance.api.client.domain.market.OrderBookEntry;
import com.binance.bot.binance.Coins;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

public class DataProvider {

    public Optional<OrderBookEntry> getPrice(Coins coin) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiRestClient client = factory.newRestClient();

        // Getting depth of a symbol
        OrderBook orderBook = client.getOrderBook(coin.getSymbol(), 10);
        System.out.println(orderBook.getAsks());

        return orderBook.getBids().stream()
                .filter(Objects::nonNull)
                .filter(orderBookEntry -> StringUtils.isNotBlank(orderBookEntry.getPrice()))
                .min((o1, o2) -> (int) ((Double.parseDouble(o1.getPrice()) - Double.parseDouble(o2.getPrice())) * 100000));
    }
}
