package com.optimaize.command4j.ext.extensions.timeout.configurabletimeout;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import com.optimaize.command4j.*;
import com.optimaize.command4j.lang.Duration;
import com.optimaize.command4j.commands.BaseCommand;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

/**
 * Allows a maximal time for the execution, or aborts by throwing a {@link TimeoutException}.
 *
 * <p>See http://en.wikipedia.org/wiki/Timeout_(computing)</p>
 *
 * <p>Configuration:
 * <pre><code>
 *     .with(TimeoutExtension.TIMEOUT, Duration.millis(5000))
 * </code></pre>
 * </p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public class TimeoutExtension implements ModeExtension {

    /**
     * If present and > 0, specifies the time to wait
     * at most for the result.
     */
    public static final Key<Duration> TIMEOUT = Key.create("timeout", Duration.class);

    @NotNull @Override
    public <A, R> Command<A, R> extend(@NotNull final Command<A, R> cmd, @NotNull Mode mode) {
        return mode.get(TIMEOUT).transform(new Function<Duration, Command<A, R>>() {
            @Override
            public Command<A, R> apply(Duration input) {
                if (input != null && input.getTime() > 0) {
                    return new Interceptor<>(cmd, input);
                }
                return cmd;
            }
        }).or(cmd);
    }

    public static class Interceptor<A, R> extends BaseCommand<A, R> {
        private final Duration duration;
        private final Command<A, R> delegate;

        public Interceptor(@NotNull Command<A, R> delegate, @NotNull Duration duration) {
            this.delegate = delegate;
            this.duration = duration;
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            try {
                return createFuture(arg, ec).get(duration.getTime(), duration.getTimeUnit());
            } catch (ExecutionException e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                }
                throw Throwables.propagate(e.getCause());
            }
        }


        protected Callable<R> createTask(final Optional<A> arg, @NotNull final ExecutionContext ec) {
            return new Callable<R>() {
                @Override
                public R call() throws Exception {
                    return delegate.call(arg, ec);
                }
            };
        }

        protected Future<R> createFuture(final Optional<A> arg, @NotNull final ExecutionContext ec) {
            return Util.newExecutor().submit(createTask(arg, ec));
        }

        @Override
        public String toString() {
            return "Timeout(" + delegate.toString() + ")";
        }
    }
}
