package org.velikokhatko.stratery1.utils;

import com.binance.api.client.domain.account.AssetBalance;
import org.velikokhatko.stratery1.constants.Constants;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    public static String assetBalanceListToString(List<AssetBalance> balances) {
        return balances.stream().map(AssetBalance::toString).collect(Collectors.joining("\n"));
    }

    public static <>

    public static String getKlinesZipURLBySymbol(String symbol, int minusMonth) {
        final LocalDate date = LocalDate.now().minusMonths(minusMonth);
        return Constants.HISTORICAL_DATA_BASE_LINK + Constants.HISTORICAL_DATA_ADDITIONAL_LINK_TEMPLATE
                .replace("{SYMBOL}", symbol)
                .replace("{YEAR}", String.valueOf(date.getYear()))
                .replace("{MONTH}", String.valueOf(date.getMonth().getValue()));
    }
}
