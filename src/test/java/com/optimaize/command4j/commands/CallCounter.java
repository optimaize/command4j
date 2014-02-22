package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Counts each execution and returns the current number of calls (including the current).
 *
 * @author Fabian Kessler
 */
public class CallCounter extends BaseCommand<Void, Integer> {

    public static class Counter {
        private volatile int i;
        public synchronized int incrementAndGet() {
            i++;
            return i;
        }
        public int get() {
            return i;
        }
    }

    private final Counter counter;
    public CallCounter(@NotNull Counter counter) {
        this.counter = counter;
    }

    @Override
    public Integer call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
        return counter.incrementAndGet();
    }

}
