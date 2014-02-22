package com.optimaize.command4j.ext.extensions.failover.autoretry;

import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Fabian Kessler
 */
public class DefaultAutoRetryStrategy implements AutoRetryStrategy {

    public static final AutoRetryStrategy INSTANCE = new DefaultAutoRetryStrategy();

    @Override @Nullable
    public Duration doRetry(int executionCounter, @NotNull Exception exception) {
        if (executionCounter!=1) {
            return null;
        }

        //TODO fabian: improve, define clearly when the default strategy says to retry.

        return Duration.millis(0);

//            if (exception instanceof ServiceException) {
//                if (((ServiceException)exception).getFaultInfo().getRetry() == Retry.NOW) {
//                    return Duration.millis(1000);
//                }
//            }
//            return null;
    }
}
