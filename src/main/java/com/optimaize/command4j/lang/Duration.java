package com.optimaize.command4j.lang;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;


/**
 * The combination of a TimeUnit and an amount in a single, immutable object.
 *
 * <p>Replace this one with the one from Guava as soon as they add it.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
@Immutable
public final class Duration {

    private final long time;
    @NotNull
    private final TimeUnit timeUnit;

    @NotNull
    public static Duration of(long time, @NotNull TimeUnit unit) {
        return new Duration(time, unit);
    }

    @NotNull
    public static Duration millis(long time) {
        return of(time, TimeUnit.MILLISECONDS);
    }

    @NotNull
    public static Duration seconds(long time) {
        return of(time, TimeUnit.SECONDS);
    }

    private Duration(long time, @NotNull TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }


    public long getTime() {
        return time;
    }

    @NotNull
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long toMillis() {
        return timeUnit.toMillis(time);
    }

    public boolean isZero() {
        return time==0;
    }

    @Override
    public String toString() {
        return "Duration[" + time + toString(timeUnit) + ']';
    }

    @NotNull
    private String toString(@NotNull TimeUnit timeUnit) {
        switch (timeUnit) {
            case MILLISECONDS:
                return "ms";
            case SECONDS:
                return "s";
            case MINUTES:
                return "m";
            case HOURS:
                return "h";
            case DAYS:
                return "d";
            default:
                return timeUnit.name();
        }
    }
}
