package com.optimaize.command4j.ext.extensions.loadlimit.maxconcurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Fabian Kessler
 */
public class MaxConcurrentStateImpl implements MaxConcurrentState {

    private AtomicInteger pending = new AtomicInteger(0);

    private final int maxPending;
    private final OnPendingFull onPendingFull;
    private final int maxWaitMillis;

    public static MaxConcurrentState withWaiting(int maxPending, int maxWaitMillis) {
        return new MaxConcurrentStateImpl(maxPending, OnPendingFull.WAIT, maxWaitMillis);
    }

    public static MaxConcurrentState withThrowing(int maxPending) {
        return new MaxConcurrentStateImpl(maxPending, OnPendingFull.THROW, 0);
    }

    private MaxConcurrentStateImpl(int maxPending, @NotNull OnPendingFull onPendingFull, int maxWaitMillis) {
        if (maxPending < 1) throw new IllegalArgumentException("maxPending must be >= 1 but was "+maxPending);
        this.maxPending = maxPending;
        this.onPendingFull = onPendingFull;
        this.maxWaitMillis = maxWaitMillis;
    }

    @Override
    public int pending() {
        return pending.get();
    }
    @Override
    public int incrementAndGet() {
        return pending.incrementAndGet();
    }
    @Override
    public int decrementAndGet() {
        return pending.decrementAndGet();
    }

    @Override
    public int maxPending() {
        return maxPending;
    }

    @Override @NotNull
    public OnPendingFull onPendingFull() {
        return onPendingFull;
    }

    @Override
    public int maxWaitMillis() {
        return maxWaitMillis;
    }

}
