package com.optimaize.command4j;

import org.jetbrains.annotations.NotNull;

/**
 * An extension to commands that can be registered with the executor. The executor
 * will call all extension's {@code extend()} method for each command it executes.
 *
 * <p>Extensions can be registered on the the CommandExecutor and Mode; the extension itself
 * must be available to the Executor, and the Mode gets it enabled/configured:
 * <pre>
 *     CommandExecutor retryExecutor = new CommandExecutorBuilder()
 *         .withExtension(new AutoRetryExtension())
 *         .build();
 *     Mode mode = Mode.create().with(AutoRetryExtension.STRATEGY, AutoRetryStrategies.alwaysOnce());
 * </pre>
 *
 * Or they can be around a single Command:
 * <pre>
 *     {@code new AutoRetryExtension.Interceptor<>(cmd, strategy)}
 * </pre>
 * </p>
 *
 * <p>Extensions usually wrap the command in question to add or alter behaviour,
 * and thus often follow the interceptor pattern.</p>
 *
 * <p>Because of the nature of the command framework that makes it easy to run
 * commands simultaneously in multiple threads, mode extension implementations
 * really should be thread safe.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public interface ModeExtension {

    /**
     * The {@link CommandExecutor} runs this for each command before executing it.
     *
     * <p>Check the existing implementations to see what to do in your implementation.</p>
     */
    @NotNull
    <A, R> Command<A, R> extend(@NotNull Command<A, R> cmd, @NotNull Mode mode);

}
