package com.optimaize.command4j.ext.extensions.failover.autoretry;

import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Fabian Kessler
 */
class AlwaysOnceInstantAutoRetryStrategy implements AutoRetryStrategy {

    public static final AutoRetryStrategy INSTANCE = new AlwaysOnceInstantAutoRetryStrategy();

    private AlwaysOnceInstantAutoRetryStrategy(){}

    @Override @Nullable
    public Duration doRetry(int executionCounter, @NotNull Exception exception) {
        if (executionCounter!=1) {
            return null;
        }
        return Duration.millis(0);
    }
}
