package com.optimaize.command4j;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An executor service for executing {@link Command}s.
 *
 * <p>This does not extend java.util.concurrent.ExecutorService, but it uses one
 * (actually a Guava {@link ListeningExecutorService}). It offers just a fraction of Guava's interface.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public interface CommandExecutorService {

    /**
     * Submits a command and returns instantly with a Future as the result.
     */
    @NotNull
    <A, V> ListenableFuture<Optional<V>> submit(@NotNull Command<A, V> cmd,
                                                @NotNull Mode mode,
                                                @Nullable A arg);

    /**
     * Executes a command and blocks until the command finishes successfully, aborts by throwing an exception,
     * the timeout occurs (which throws an exception too), or the command is cancelled before any of these occur.
     */
    @NotNull
    <A, V> Optional<V> submitAndWait(@NotNull Command<A, V> cmd,
                                     @NotNull Mode mode,
                                     @Nullable A arg,
                                     @NotNull Duration timeout) throws InterruptedException, TimeoutException, Exception;

    /**
     * Returns the service on which this command executor operates.
     * Things like shutdown() and awaitTermination() can be done on it.
     *
     * <p>This is beta api. It is not well defined yet whether access to it remains, or methods such as shutdown()
     * well be added to this interface. One argument is that the userland has access to this object anyway since
     * he's the one putting it in.</p>
     */
    @Beta
    ListeningExecutorService getUnderlyingExecutor();
}
