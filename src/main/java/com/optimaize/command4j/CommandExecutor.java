package com.optimaize.command4j;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

/**
 * The executor that can execute commands either directly using {@link #execute} or with a
 * {@link CommandExecutorService} see {@link #service}.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public interface CommandExecutor {

    /**
     * Executes it in the current thread, and blocks until it either finishes successfully or aborts by
     * throwing an exception.
     *
     * @param <A> argument
     * @param <R> Result
     */
    @NotNull
    <A, R> Optional<R> execute(@NotNull Command<A, R> cmd, @NotNull Mode mode, @Nullable A arg) throws Exception;

    /**
     * Creates a new command executor service based on your <code>executorService</code> and returns it.
     */
    @NotNull
    CommandExecutorService service(@NotNull final ExecutorService executorService);

    /**
     * @return The same default executor service on each call. It is a simple single-threaded service.
     *         For anything beyond testing you should use {@link #service(java.util.concurrent.ExecutorService)}.
     */
    @NotNull
    CommandExecutorService service();

}
