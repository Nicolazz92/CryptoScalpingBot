package com.binance.bot.binance.services;

import com.binance.api.client.domain.market.OrderBookEntry;
import com.binance.bot.binance.Coins;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class DataProviderTest {

    @Test
    void getPrice() {
        DataProvider dataProvider = new DataProvider();
        Optional<OrderBookEntry> price = dataProvider.getPrice(Coins.XRP_USD);
        Assertions.assertTrue(price.isPresent());
        System.out.println(price.get());
    }
}