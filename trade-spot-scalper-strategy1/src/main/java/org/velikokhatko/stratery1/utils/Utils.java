package org.velikokhatko.stratery1.utils;

import com.binance.api.client.domain.account.AssetBalance;
import org.velikokhatko.stratery1.constants.Constants;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    public static String assetBalanceListToString(List<AssetBalance> balances) {
        return balances.stream()
                .map(Utils::toString)
                .collect(Collectors.joining("\n"));
    }

    private static String toString(AssetBalance assetBalance) {
        return String.format("%s: free=%s, locked=%s", assetBalance.getAsset(), assetBalance.getFree(), assetBalance.getLocked());
    }

    public static String getKlinesZipURLBySymbol(String symbol, int minusDays) {
        final LocalDate date = LocalDate.now().minusDays(minusDays);
        return Constants.HISTORICAL_DATA_BASE_LINK + Constants.HISTORICAL_DATA_ADDITIONAL_LINK_TEMPLATE
                .replace("{SYMBOL}", symbol)
                .replace("{YEAR}", String.valueOf(date.getYear()))
                .replace("{MONTH}", String.format("%02d", date.getMonth().getValue()))
                .replace("{DAY}", String.format("%02d", date.getDayOfMonth()));
    }
}
