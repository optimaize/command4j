package com.optimaize.command4j.impl;

import com.optimaize.command4j.*;
import com.optimaize.command4j.cache.ExecutorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Runs a setup command.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
class Runner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);
    private final CommandExecutor executor;
    private final ExecutorCache cache;

    Runner(CommandExecutor executor, ExecutorCache cache) {
        this.executor = executor;
        this.cache = cache;
    }

    @NotNull
    public <A, V> Optional<V> run(@NotNull Command<A, V> cmd, @NotNull Mode mode, @NotNull Optional<A> arg) throws Exception {
        log.trace("Executing command '{}' in mode: '{}'", cmd, mode);

        // here you can implement any global default behaviour around the command,
        // dependent on mode values or not

        ExecutionContext ctx = new ContextImpl(mode);
        return Optional.fromNullable(cmd.call(arg, ctx));
    }

    private class ContextImpl implements ExecutionContext {
        private final Mode mode;

        private ContextImpl(Mode mode) {
            this.mode = mode;
        }

        @NotNull @Override
        public Mode getMode() {
            return mode;
        }

        @NotNull @Override
        public <A, R> Optional<R> execute(@NotNull Command<A, R> task, Optional<A> arg) throws Exception {
            return Optional.fromNullable(task.call(arg, this));
        }

        @NotNull
        @Override
        public <A, R> Optional<R> execute(@NotNull Command<A, R> cmd, @NotNull Mode mode, Optional<A> arg) throws Exception {
            return executor.execute(cmd, mode, arg.orNull());
        }

        @NotNull @Override
        public ExecutorCache getCache() {
            return cache;
        }
    }
}
