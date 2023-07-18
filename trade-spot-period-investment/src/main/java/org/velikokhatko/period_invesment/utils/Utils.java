package org.velikokhatko.period_invesment.utils;

import com.binance.api.client.domain.account.AssetBalance;
import org.velikokhatko.period_invesment.constants.Constants;

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
}
