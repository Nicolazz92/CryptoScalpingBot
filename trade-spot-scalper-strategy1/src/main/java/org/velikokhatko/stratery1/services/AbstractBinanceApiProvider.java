package org.velikokhatko.stratery1.services;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;

import java.util.stream.Collectors;

public abstract class AbstractBinanceApiProvider {

    protected BinanceApiRestClient client;

    public String getBalance() {
        Account account = client.getAccount();
        return account.getBalances().stream().map(AssetBalance::toString).collect(Collectors.joining("\n"));
    }
}
