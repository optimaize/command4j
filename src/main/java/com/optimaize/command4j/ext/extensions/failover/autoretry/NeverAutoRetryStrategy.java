package com.optimaize.command4j.ext.extensions.failover.autoretry;

import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Fabian Kessler
 */
class NeverAutoRetryStrategy implements AutoRetryStrategy {

    public static final AutoRetryStrategy INSTANCE = new NeverAutoRetryStrategy();

    private NeverAutoRetryStrategy(){}

    @Override @Nullable
    public Duration doRetry(int executionCounter, @NotNull Exception exception) {
        return null;
    }
}
