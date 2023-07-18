package org.velikokhatko.period_invesment.constants;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public final class Constants {
    public static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.0#");
    public static final DecimalFormat DOUBLE_VERBOSE_FORMAT = new DecimalFormat("#.0000#");

    public static final Duration DURATION_ONE_DAY = Duration.of(1, ChronoUnit.DAYS);
    public static final Duration DURATION_FIVE_DAYS = Duration.of(5, ChronoUnit.DAYS);

    public static final String CRON_EVERY_MINUTE = "0 * * * * *";
}
