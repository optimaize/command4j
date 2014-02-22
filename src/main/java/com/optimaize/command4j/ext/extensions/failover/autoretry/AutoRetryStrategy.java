package com.optimaize.command4j.ext.extensions.failover.autoretry;

import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Fabian Kessler
 */
public interface AutoRetryStrategy {

    /**
     * Decides if auto-retry should take place.
     * @param executionCounter When this method is called for the first time the number is 1 (one
     *                         execution failed already). The second time it's 2, and so on.
     *                         You need to return <code>null</code> when this number gets too high to
     *                         prevent endless retrying and waiting.
     *                         Also, you may increase the duration as the counter increases.
     * @param exception The exception that occurred. The method is encouraged to respond based on this.
     * @return <code>null</code> to not retry, otherwise a Duration for how long to wait until the retry
     *         should take place.
     */
    @Nullable
    Duration doRetry(int executionCounter, @NotNull Exception exception);
}
