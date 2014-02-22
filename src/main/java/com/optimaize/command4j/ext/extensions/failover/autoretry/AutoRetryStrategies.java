package com.optimaize.command4j.ext.extensions.failover.autoretry;

import org.jetbrains.annotations.NotNull;

/**
 * @author Fabian Kessler
 */
public class AutoRetryStrategies {

    @NotNull
    public static AutoRetryStrategy defaultStrategy() {
        return DefaultAutoRetryStrategy.INSTANCE;
    }

    @NotNull
    public static AutoRetryStrategy alwaysOnce() {
        return AlwaysOnceInstantAutoRetryStrategy.INSTANCE;
    }

    @NotNull
    public static AutoRetryStrategy never() {
        return NeverAutoRetryStrategy.INSTANCE;
    }

}
