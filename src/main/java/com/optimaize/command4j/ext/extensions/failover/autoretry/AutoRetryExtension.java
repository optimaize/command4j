package com.optimaize.command4j.ext.extensions.failover.autoretry;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.ModeExtension;
import com.optimaize.command4j.commands.BaseCommandInterceptor;
import com.optimaize.command4j.lang.Duration;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automatically retries the command once if it failed and the strategy says yes.
 * Tries it in exactly the same way, using the same Mode.
 *
 * <p>Configuration:
 * <pre><code>
 *     .with(AutoRetryExtension.strategy(), new MyAutoRetryStrategy())
 *     .with(AutoRetryExtension.strategy(), AutoRetryStrategies.defaultStrategy())
 * </code></pre>
// * The strategy is optional, if omitted then the {@link AutoRetryStrategies#defaultStrategy} is used.
 * </p>
 *
 * <p>Exceptions thrown:
 * <ul>
 *   <li>If the call fails and auto-retry does not happen (strategy says no) then the exception from the
 *       last call is thrown.</li>
 *   <li>If an exception occurs in userland code when calling strategy.doRetry() then this exception
 *       is logged as an error, and the exception from the call is thrown.</li>
 *   <li>If auto-retry should happen, but an InterruptedException happens while waiting before running the command
 *       again, then the last exception is thrown and no one will know that an interrupt occurred.
 *       (TODO Eike: do you agree?)</li>
 *   <li>If Integer.MAX_VALUE is reached in the loop then the exception from the last call is thrown.</li>
 * </ul>
 * Thus: you will not see the previous exceptions if a later call succeeds or fails.
 * </p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public class AutoRetryExtension implements ModeExtension {

    private static final Logger logger = LoggerFactory.getLogger(AutoRetryExtension.class);
    public static final Key<AutoRetryStrategy> STRATEGY = Key.create("AutoRetryExtension.strategy", AutoRetryStrategy.class);


    @NotNull @Override
    public <A, R> Command<A, R> extend(@NotNull Command<A, R> cmd, @NotNull Mode mode) {
        Optional<AutoRetryStrategy> optional = mode.get(STRATEGY);
        if (optional.isPresent()) {
            AutoRetryStrategy strategy = optional.get();
            cmd = new Interceptor<>(cmd, strategy);
        }
        return cmd;
    }

    public static class Interceptor<A, R> extends BaseCommandInterceptor<A, R> {
        @NotNull
        private final AutoRetryStrategy strategy;

        /**
         * Overloaded method that uses the {@link AutoRetryStrategies#defaultStrategy}.
         */
        public Interceptor(@NotNull Command<A, R> cmd) {
            this(cmd, AutoRetryStrategies.defaultStrategy());
        }
        /**
         * @param cmd The command to execute.
         */
        public Interceptor(@NotNull Command<A, R> cmd, @NotNull AutoRetryStrategy strategy) {
            super(cmd);
            this.strategy = strategy;
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            Exception lastCommandEx = null;
            for (int i=1; i<Integer.MAX_VALUE; i++) {
                try {
                    return delegate.call(arg, ec);
                } catch (Exception e) { //yes we don't catch Throwable
                    lastCommandEx = e;

                    Duration retryIn;
                    try {
                        retryIn = strategy.doRetry(i, e);
                    } catch (Exception exInUserland) {
                        //as documented in the class header.
                        logger.error("Exception in userland code when calling strategy.doRetry()!", exInUserland);
                        throw lastCommandEx;
                    }
                    if (retryIn==null) { //don't retry
                        throw e;
                    } else {
                        if (!retryIn.isZero()) {
                            try {
                                Thread.sleep(retryIn.toMillis());
                            } catch (InterruptedException e1) {
                                //apparently someone run out of patience.
                                //throw the original exception (as documented in the class header).
                                throw e;
                            }
                        }
                    }
                }
            }
            assert lastCommandEx!=null;
            throw lastCommandEx;
        }

    }

}
