package com.optimaize.command4j.impl;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.CommandExecutorService;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.lang.Duration;
import com.optimaize.command4j.lang.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.concurrent.*;

/**
 * @author Eike Kettner
 */
public class DefaultCommandExecutorService implements CommandExecutorService {

    @NotNull
    private final Logger log;
    @NotNull
    private final Runner runner;
    @NotNull
    private final ExtensionHandler extensionHandler;
    @NotNull
    private final ListeningExecutorService executorService;

    public DefaultCommandExecutorService(@NotNull Logger log, @NotNull Runner runner, @NotNull ExtensionHandler handler, @NotNull ExecutorService executorService) {
        this.log = log;
        this.runner = runner;
        this.extensionHandler = handler;
        this.executorService = MoreExecutors.listeningDecorator(executorService);
    }

    /**
     * Shuts down the executor service.
     * Blocks and awaits termination for up to 10 seconds before returning.
     * TODO Eike: should it be possible to pass in this timeout? The Duration class could be used for it.
     */
    public void shutdown() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Throwable e) {
            log.error("Interrupted during shutdown.", e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    @NotNull @Override
    public <A, V> ListenableFuture<Optional<V>> submit(@NotNull Command<A, V> cmd, @NotNull Mode mode, @Nullable A arg) {
        //I'd apply modes in the calling thread, since it shouldn't be expensive and it allows to pull
        //values from the current thread, that might be necessary (to reconstruct ThreadLocals..)
        Tuple2<Command<A, V>, Mode> wrapped = extensionHandler.applyModes(cmd, mode);
        return executorService.submit(new CommandTask<>(wrapped._1, wrapped._2, arg));
    }

    @NotNull @Override
    public <A, V> Optional<V> submitAndWait(@NotNull Command<A, V> cmd, @NotNull Mode mode, A arg, @NotNull Duration timeout) throws Exception, InterruptedException, TimeoutException {
        ListenableFuture<Optional<V>> future = submit(cmd, mode, arg);
        try {
            return future.get(timeout.getTime(), timeout.getTimeUnit());
        } catch (ExecutionException e) {
            //TODO eike: this if block was active, but that's not needed is it? the pattern would be to wrap
            //a checked ex in a runtime ... but that's not done and not needed. am i missing something?
//            if (e.getCause() instanceof Exception) {
//                throw (Exception) e.getCause();
//            }
            throw Throwables.propagate(e.getCause());
        }
    }

    @Override
    public ListeningExecutorService getUnderlyingExecutor() {
        return executorService;
    }

    private final class CommandTask<A, R> implements Callable<Optional<R>> {

        private final Command<A, R> task;
        private final Mode mode;
        private final A arg;

        private CommandTask(Command<A, R> task, Mode mode, A arg) {
            this.task = task;
            this.mode = mode;
            this.arg = arg;
        }

        private void setupThread() {
        }

        private void cleanupThread() {
        }

        @Override @NotNull
        public Optional<R> call() throws Exception {
            try {
                setupThread();
                return runner.run(task, mode, Optional.fromNullable(arg));
            } finally {
                cleanupThread();
            }
        }
    }
}
