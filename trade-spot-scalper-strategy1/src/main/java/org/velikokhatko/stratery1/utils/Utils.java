package org.velikokhatko.stratery1.utils;

import com.binance.api.client.domain.account.AssetBalance;

import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    public static String assetBalanceToString(List<AssetBalance> balances) {
        return balances.stream().map(AssetBalance::toString).collect(Collectors.joining("\n"));
    }
}
