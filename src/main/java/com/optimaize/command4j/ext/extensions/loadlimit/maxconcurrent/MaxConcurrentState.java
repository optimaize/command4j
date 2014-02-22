package com.optimaize.command4j.ext.extensions.loadlimit.maxconcurrent;

import org.jetbrains.annotations.NotNull;

/**
 * Defines what to do when the limit of concurrently executing commands has been reached, and contains the state
 * (the amount of currently pending requests).
 *
 * @author Fabian Kessler
 */
public interface MaxConcurrentState {

    /**
     * Defines what to do when the amount of concurrent executions has been reached.
     *
     * <p>This does not offer a QUEUE strategy because well that is kinda too late to implement.
     * If you figure out how to do it nicely, feel free...</p>
     */
    public enum OnPendingFull {
        /**
         * Keeps trying for a configured maximal amount of time, then gives up by throwing.
         */
        WAIT,
        /**
         * Throws directly.
         */
        THROW;

        public static void assertSize(int expected) {
            assert values().length==expected : "Update the code calling this with "+expected+"!";
        }
    }

    int pending();
    int incrementAndGet();
    int decrementAndGet();

    /**
     * Tells how many are allowed to execute concurrently at most.
     */
    int maxPending();

    /**
     * Tells what to do when the amount of concurrent executions has been reached.
     */
    @NotNull
    OnPendingFull onPendingFull();

    /**
     * If {@link #onPendingFull()} is WAIT then this defines for how long to wait at most until giving up.
     */
    int maxWaitMillis();

}
