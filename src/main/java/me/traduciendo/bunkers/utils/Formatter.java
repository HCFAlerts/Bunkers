package me.traduciendo.bunkers.utils;


import me.traduciendo.bunkers.utils.extra.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Formatter {

    private static DecimalFormat DTR_FORMATTER;
    private static final DecimalFormat HEALTH_FORMATTER = new DecimalFormat("#.#");
    private static final DecimalFormat BARD_ENERGY_FORMATTER = new DecimalFormat("0.0");

    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1L);
    private static final long HOUR = TimeUnit.HOURS.toMillis(1L);
    private static final long DAY = TimeUnit.DAYS.toMillis(1L);

    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM hh:mm");

    public static String getRemaining(long duration, boolean milliseconds) {
        if (milliseconds && duration < MINUTE) {
            return REMAINING_SECONDS_TRAILING.get().format(duration * 0.001) + 's';
        } else {
            return (duration <= 0 ? "00:00" : DurationFormatUtils.formatDuration(duration, (duration >= HOUR ? "HH:" : "") + "mm:ss"));
        }
    }

    public static Long parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        if (input.equals("0")) {
            return 0L;
        }

        long result = 0L;
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                String str;
                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }

        return (result == 0L || result == -1L ? null : result);
    }

    private static long convert(int value, char unit) {
        switch (unit) {
            case 'y': {
                return value * TimeUnit.DAYS.toMillis(365L);
            }
            case 'M': {
                return value * TimeUnit.DAYS.toMillis(30L);
            }
            case 'd': {
                return value * TimeUnit.DAYS.toMillis(1L);
            }
            case 'h': {
                return value * TimeUnit.HOURS.toMillis(1L);
            }
            case 'm': {
                return value * TimeUnit.MINUTES.toMillis(1L);
            }
            case 's': {
                return value * TimeUnit.SECONDS.toMillis(1L);
            }
            default: {
                return -1L;
            }
        }
    }

    public static String formatDetailed(long time) {
        if (time == 0L) {
            return "0s";
        }

        return DurationFormatUtils.formatDurationWords(
                time, true, true
        );
    }

    public static String formatSchedule(long toFormat) {
        if (toFormat > DAY) {
            return DurationFormatUtils.formatDuration(toFormat, "d'd, 'H'h'");

        } else if (toFormat > HOUR) {
            return DurationFormatUtils.formatDuration(toFormat, "H'h, 'm'm'");

        } else return DurationFormatUtils.formatDuration(toFormat, "m'm, 's's'");
    }

    public static String formatDtr(double dtr) {
        return DTR_FORMATTER.format(dtr);
    }

    public static String formatBardEnergy(double energy) {
        return BARD_ENERGY_FORMATTER.format(energy);
    }

    public static void loadDTRFormat() {
        DTR_FORMATTER = new DecimalFormat("0.00");
    }

    public static String formatHealth(double health) {
        return HEALTH_FORMATTER.format(health);
    }
}