package com.optimaize.command4j.ext.extensions.loadlimit.maxconcurrent;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.ModeExtension;
import com.optimaize.command4j.commands.BaseCommandInterceptor;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Counts the number of pending requests and waits or throws until a slot becomes available.
 *
 * <p>Use Cases:
 * This extension may come in handy to prevent overload when you're <b>not in direct control</b> over the executed
 * commands or their execution services, but can provide the Mode.
 * If you are in control, for example by using your own {@link com.optimaize.command4j.CommandExecutorService}, then
 * you don't need this, instead you control the number of simultaneous executions with the thread pool.
 * </p>
 *
 * <p>Configuration:
 * <pre><code>
 *     with(MaxConcurrentRunningExtension.STRATEGY, MaxConcurrentStateImpl.withWaiting(maxAtATime, maxTimeout))
 * </code></pre>
 * </p>
 *
 * @author Fabian Kessler
 */
public class MaxConcurrentRunningExtension implements ModeExtension {

    /**
     */
    public static final Key<MaxConcurrentState> STRATEGY = Key.create("MaxConcurrentRequestsExtension.strategy", MaxConcurrentState.class);

    @NotNull @Override
    public <A, R> Command<A, R> extend(@NotNull final Command<A, R> cmd, @NotNull final Mode mode) {
        return mode.get(STRATEGY).transform(new Function<MaxConcurrentState, Command<A, R>>() {
            @Override
            public Command<A, R> apply(MaxConcurrentState strategy) {
                if (strategy != null) {
                    return new Interceptor<>(cmd, strategy);
                }
                return cmd;
            }
        }).or(cmd);
    }

    public static class Interceptor<A, R> extends BaseCommandInterceptor<A, R> {
        private final MaxConcurrentState strategy;

        public Interceptor(@NotNull Command<A, R> delegate, MaxConcurrentState strategy) {
            super(delegate);
            this.strategy = strategy;
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            boolean didIncrement = false;
            try {
                didIncrement = beforeRequest();
                return delegate.call(arg, ec);
            } finally {
                afterRequest(didIncrement);
            }
        }

        /**
         * Impl note: avoiding to synchronize here by optimisticly incrementing, and then checking, and
         * decrementing if necessary.
         * @return true if incrementation was done (otherwise it throws).
         */
        private boolean beforeRequest() {
            int amount = strategy.incrementAndGet();
            if (amount <= strategy.maxPending()) {
                //let it pass
                return true;
            } else {
                //set back to what it was. note that it can now be under the strategy's maxPending in case
                //someone else decremented almost simultaneously.
                strategy.decrementAndGet();
                MaxConcurrentState.OnPendingFull.assertSize(2);
                switch (strategy.onPendingFull()) {
                    case THROW:
                        throw new RuntimeException("Already "+strategy.maxPending()+" commands pending, refusing to start more!");
                    case WAIT:
                        int maxWaitMillis = strategy.maxWaitMillis();
                        Stopwatch stopwatch = Stopwatch.createStarted();
                        while (true) {
                            try {
                                Thread.sleep( Math.min(10, maxWaitMillis) );
                            } catch (InterruptedException e) {
                                //never mind
                            }
                            //check again
                            amount = strategy.incrementAndGet();
                            if (amount <= strategy.maxPending()) {
                                //let it pass
                                return true;
                            } else {
                                //set increment back:
                                strategy.decrementAndGet();

                                //check max wait time:
                                long timeWaitedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                                if (timeWaitedMillis >= maxWaitMillis) {
                                    //giving up
                                    throw new RuntimeException("Already "+strategy.maxPending()+" commands pending, waited "+timeWaitedMillis+"ms, giving up!");
                                }
                            }
                        }
                    default:
                        throw new UnsupportedOperationException("Unknown value: "+strategy.onPendingFull());
                }
            }
        }

        private void afterRequest(boolean didIncrement) {
            if (didIncrement) {
                int i = strategy.decrementAndGet();
                assert i >= 0 : "Programming error: currently open requests cannot be negative, value was: "+i+"!";
            }
        }

        @Override
        public String toString() {
            return "MaxConcurrentRequests(" + delegate.toString() + ")";
        }
    }
}
