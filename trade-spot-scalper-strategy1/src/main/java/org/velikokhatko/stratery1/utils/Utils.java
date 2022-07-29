package org.velikokhatko.stratery1.utils;

import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolInfo;
import org.velikokhatko.stratery1.constants.Constants;
import velikokhatko.dto.SymbolInfoShortDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    public static String assetBalanceListToString(List<AssetBalance> balances) {
        return balances.stream()
                .map(Utils::toString)
                .collect(Collectors.joining("\n"));
    }

    private static String toString(AssetBalance assetBalance) {
        final String free = Constants.DOUBLE_VERBOSE_FORMAT.format(Double.valueOf(assetBalance.getFree()));
        final String locked = Constants.DOUBLE_VERBOSE_FORMAT.format(Double.valueOf(assetBalance.getLocked()));
        return String.format("%s: free=%s, locked=%s", assetBalance.getAsset(), free, locked);
    }

    public static String getKlinesZipURLBySymbol(String symbol, int minusDays) {
        final LocalDate date = LocalDate.now().minusDays(minusDays);
        return Constants.HISTORICAL_DATA_BASE_LINK + Constants.HISTORICAL_DATA_ADDITIONAL_LINK_TEMPLATE
                .replace("{SYMBOL}", symbol)
                .replace("{YEAR}", String.valueOf(date.getYear()))
                .replace("{MONTH}", String.format("%02d", date.getMonth().getValue()))
                .replace("{DAY}", String.format("%02d", date.getDayOfMonth()));
    }

    /**
     * 0.1% - самая большая комиссия
     *
     * @param resultMoney до уплаты комиссии
     * @return после уплаты комиссии
     */
    public static double minusFee(double resultMoney) {
        return resultMoney / 1000 * 999;
    }

    public static LocalDateTime truncate(LocalDateTime ldt) {
        return ldt.truncatedTo(ChronoUnit.MINUTES);
    }

    public static SymbolInfoShortDTO extractSymbolInfoShort(SymbolInfo symbolInfo) {
        SymbolInfoShortDTO result = new SymbolInfoShortDTO();
        result.setSymbol(symbolInfo.getSymbol());
        result.setBaseAsset(symbolInfo.getBaseAsset());
        result.setLotSizeMin(Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.LOT_SIZE).getMinQty()));
        result.setMarketLotSizeMin(Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.MARKET_LOT_SIZE).getMinQty()));
        return result;
    }
}
