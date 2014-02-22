package com.optimaize.command4j;

import com.google.common.base.Optional;
import com.optimaize.command4j.cache.ExecutorCache;
import org.jetbrains.annotations.NotNull;

/**
 * The execution context is an object created by the executor that lives
 * as long as the command is executing. It gives the command implementation
 * access to the command framework and a global {@link ExecutorCache cache}.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public interface ExecutionContext {

    /**
     * @return the mode that was supplied to the initial invocation.
     */
    @NotNull
    Mode getMode();

    /**
     * @return the global cache to store/retrieve objects to/from.
     */
    @NotNull
    ExecutorCache getCache();

    /**
     * Overloaded method that uses the same <code>mode</code> as supplied
     * to the parent invocation.
     *
     * @see #execute(Command, Mode, com.google.common.base.Optional)
     */
    @NotNull
    <A, R> Optional<R> execute(@NotNull Command<A, R> task, Optional<A> arg) throws Exception;

    /**
     * Runs the given <code>command</code> using the given <code>mode</code>.
     *
     * <p>This method is for commands that want to execute yet another command.
     * One example is an auto-retry extension that executes the command again.
     * </p>
     */
    @NotNull
    <A, R> Optional<R> execute(@NotNull Command<A, R> task, @NotNull Mode mode, Optional<A> arg) throws Exception;

}
